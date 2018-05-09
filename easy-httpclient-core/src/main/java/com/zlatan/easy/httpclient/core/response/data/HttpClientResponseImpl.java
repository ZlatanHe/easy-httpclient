package com.zlatan.easy.httpclient.core.response.data;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 10:50 AM
 */
public final class HttpClientResponseImpl<T> implements HttpClientResponse<T> {

  private HttpResponse httpResponse;

  private Map<String, List<HttpClientResponseHeader>> headers;

  private T body;

  public HttpClientResponseImpl(HttpResponse httpResponse,
                                T body) {
    this.httpResponse = httpResponse;
    this.body = body;
    this.headers = new HashMap<>();
    for (Header header : httpResponse.getAllHeaders()) {
      List<HttpClientResponseHeader> headerList = headers.computeIfAbsent(
              header.getName(),
              key -> new ArrayList<>()
      );
      headerList.add(new HttpClientResponseHeaderImpl(header));
    }
  }

  @Override
  public int getStatusCode() {
    return httpResponse.getStatusLine().getStatusCode();
  }

  @Override
  public List<HttpClientResponseHeader> getHeaders(String name) {
    return headers.get(name);
  }

  @Override
  public HttpClientResponseHeader getFirstHeader(String name) {
    List<HttpClientResponseHeader> headerList = headers.get(name);
    return CollectionUtils.isEmpty(headerList) ? null : headerList.get(0);
  }

  @Override
  public HttpClientResponseHeader getLastHeader(String name) {
    List<HttpClientResponseHeader> headerList = headers.get(name);
    return CollectionUtils.isEmpty(headerList) ? null : headerList.get(headerList.size() - 1);
  }

  @Override
  public T getBody() {
    return body;
  }
}
