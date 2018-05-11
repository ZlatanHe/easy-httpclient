package com.zlatan.easy.httpclient.mapper.core;

import com.zlatan.easy.httpclient.core.HttpClientEngine;
import com.zlatan.easy.httpclient.mapper.configuration.EasyHttpConfiguration;
import com.zlatan.easy.httpclient.mapper.core.template.InterfaceTemplate;
import com.zlatan.easy.httpclient.mapper.core.template.RequestTemplateFactory;
import org.apache.http.client.HttpClient;

import java.lang.reflect.Method;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 6:39 PM
 */
@SuppressWarnings("unchecked")
public final class HttpClientMapperBuilder<T> {

  private final HttpClient globalClient;

  private final EasyHttpConfiguration.ProxyConfiguration globalProxyConfig;

  private final Method method;

  public HttpClientMapperBuilder(HttpClient globalClient,
                                 EasyHttpConfiguration.ProxyConfiguration globalProxyConfig,
                                 Method method) {
    this.globalClient = globalClient;
    this.globalProxyConfig = globalProxyConfig;
    this.method = method;
  }

  public HttpClientMapper<T> build() {
    return args -> {
      InterfaceTemplate interfaceTemplate = RequestTemplateFactory
              .buildTemplate(method)
              .setGlobalProxyConfig(globalProxyConfig);
      return new HttpClientEngine<>(
              globalClient,
              interfaceTemplate.constructRequestBuilder(args),
              interfaceTemplate.constructResponseBuilder()
      )
              .requireLog()
              .execute();
    };
  }
}
