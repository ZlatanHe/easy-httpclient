package com.zlatan.easy.httpclient.core;

import com.zlatan.easy.httpclient.core.exception.runtime.HttpClientExecutionException;
import com.zlatan.easy.httpclient.core.request.HttpRequestBuilder;
import com.zlatan.easy.httpclient.core.response.HttpResponseBuilder;
import com.zlatan.easy.httpclient.core.response.data.HttpClientResponse;
import com.zlatan.easy.httpclient.core.util.HttpClientLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 04/05/2018 6:06 PM
 */
@Slf4j
public final class HttpClientEngine<T> {

  private volatile boolean logRequired;

  private final HttpClient httpClient;

  private final HttpRequestBuilder httpRequestBuilder;

  private final HttpResponseBuilder<T> httpResponseBuilder;

  public HttpClientEngine(HttpClient httpClient,
                          HttpRequestBuilder httpRequestBuilder,
                          HttpResponseBuilder<T> httpResponseBuilder) {
    this.httpClient = httpClient;
    this.httpRequestBuilder = httpRequestBuilder;
    this.httpResponseBuilder = httpResponseBuilder;
  }

  public HttpClientEngine<T> requireLog() {
    this.logRequired = true;
    return this;
  }

  public HttpClientResponse<T> execute() {
    HttpRequestBase httpRequest = httpRequestBuilder.build();
    if (logRequired) {
      logRequest(httpRequest);
    }
    HttpResponse httpResponse;
    try {
      httpResponse = httpClient.execute(httpRequest);
    } catch (Exception e) {
      throw new HttpClientExecutionException(e);
    }
    HttpClientResponse<T> result = httpResponseBuilder.build(httpResponse);
    if (logRequired) {
      logResponse(httpResponse, result);
    }
    return result;
  }

  private void logRequest(HttpRequestBase request) {
    HttpClientLogUtils.printHttpRequestInfo(log, request);
  }

  private void logResponse(HttpResponse httpResponse,
                           HttpClientResponse result) {
    HttpClientLogUtils.printHttpResponseInfo(log, httpResponse, result.getBody());
  }
}
