package gobblin.writer;

import org.apache.avro.generic.GenericRecord;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import lombok.extern.slf4j.Slf4j;

import gobblin.http.ApacheHttpClient;
import gobblin.http.ApacheHttpResponseHandler;
import gobblin.http.HttpRequestBuilder;
import gobblin.utils.HttpConstants;


@Slf4j
public class AvroHttpWriterBuilder extends AsyncHttpWriterBuilder<GenericRecord, HttpUriRequest, CloseableHttpResponse> {

  private static final Config FALLBACK =
      ConfigFactory.parseMap(ImmutableMap.<String, Object>builder()
          .put(HttpConstants.CONTENT_TYPE, "application/json")
          .build());

  @Override
  public AvroHttpWriterBuilder fromConfig(Config config) {
    config = config.withFallback(FALLBACK);
    ApacheHttpClient client = new ApacheHttpClient(HttpClientBuilder.create(), config, broker);
    this.client = client;

    String urlTemplate = config.getString(HttpConstants.URL_TEMPLATE);
    String verb = config.getString(HttpConstants.VERB);
    String contentType = config.getString(HttpConstants.CONTENT_TYPE);
    this.asyncRequestBuilder = new HttpRequestBuilder(urlTemplate, verb, contentType);
    this.responseHandler = new ApacheHttpResponseHandler();
    return this;
  }
}
