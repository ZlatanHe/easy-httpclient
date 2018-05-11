package com.zlatan.easy.httpclient.mapper.support;

import com.zlatan.easy.httpclient.core.proxy.ProxyBinder;
import com.zlatan.easy.httpclient.core.proxy.ProxyConfig;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 11/05/2018 8:14 PM
 */
public class SimpleHttpProxyBinder implements ProxyBinder {

  @Override
  public void bind(ProxyConfig config, HttpRequestBase base) throws Exception {
    URI uri = base.getURI();
    String url = uri.toString();
    String host = uri.getHost();
    String scheme = uri.getScheme();
    String remainUrl = url.substring(url.indexOf(host) + host.length(), url.length());
    url = config.getScheme() + "://" + config.getProxyHostName() + ":" + config.getPort() + remainUrl;
    base.setURI(URI.create(url));
    base.setHeader("Scheme", scheme);
    base.setHeader("Host", host);
  }
}
