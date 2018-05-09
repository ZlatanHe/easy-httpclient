package com.zlatan.easy.httpclient.core.response.data;

import java.util.List;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 10:59 AM
 */
public interface HttpClientResponse<T> {

  int getStatusCode();

  List<HttpClientResponseHeader> getHeaders(String name);

  HttpClientResponseHeader getFirstHeader(String name);

  HttpClientResponseHeader getLastHeader(String name);

  T getBody();
}
