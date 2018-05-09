package com.zlatan.easy.httpclient.core.response.data;

import org.apache.http.Header;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 11:32 AM
 */
public final class HttpClientResponseHeaderImpl implements HttpClientResponseHeader {

  private Header header;

  public HttpClientResponseHeaderImpl(Header header) {
    this.header = header;
  }

  @Override
  public String getName() {
    return header.getName();
  }

  @Override
  public String getValue() {
    return header.getValue();
  }
}
