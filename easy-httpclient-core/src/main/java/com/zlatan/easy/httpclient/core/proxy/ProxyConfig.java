package com.zlatan.easy.httpclient.core.proxy;

import lombok.Getter;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 07/05/2018 4:56 PM
 */
@Getter
public class ProxyConfig {

  public static final int DEFAULT_PORT = 80;

  public static final String HTTP_SCHEME = "http";

  public static final String HTTPS_SCHEME = "https";

  private final String proxyHostName;

  private final int port;

  private final String scheme;

  public ProxyConfig(String proxyHostName) {
    this.proxyHostName = proxyHostName;
    this.port = DEFAULT_PORT;
    this.scheme = HTTP_SCHEME;
  }

  public ProxyConfig(String proxyHostName, String scheme) {
    this.proxyHostName = proxyHostName;
    this.port = DEFAULT_PORT;
    this.scheme = scheme;
  }

  public ProxyConfig(String proxyHostName, int port, String scheme) {
    this.proxyHostName = proxyHostName;
    this.port = port;
    this.scheme = scheme;
  }
}
