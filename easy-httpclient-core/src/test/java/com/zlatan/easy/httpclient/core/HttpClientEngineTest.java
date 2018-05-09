package com.zlatan.easy.httpclient.core;

import com.zlatan.easy.httpclient.core.exception.runtime.HttpClientRuntimeException;
import com.zlatan.easy.httpclient.core.proxy.ProxyBinder;
import com.zlatan.easy.httpclient.core.proxy.ProxyConfig;
import com.zlatan.easy.httpclient.core.request.HttpRequestBuilder;
import com.zlatan.easy.httpclient.core.response.HttpResponseBuilder;
import com.zlatan.easy.httpclient.core.response.data.HttpClientResponse;
import com.zlatan.easy.httpclient.core.response.decode.SimpleStringBodyDecoder;
import com.zlatan.easy.httpclient.core.util.BeanCacheUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 2:52 PM
 */
public class HttpClientEngineTest {

  private HttpClient httpClient;

  private static final ProxyConfig YOUZAN_QA_PROXY =
          new ProxyConfig("proxy-qa.s.qima-inc.com", ProxyConfig.HTTPS_SCHEME);

  private static final ProxyBinder YOUZAN_DEFAULT_PROXY_BINDER =
          (config, base) -> {
            URI uri = base.getURI();
            String url = uri.toString();
            String host = uri.getHost();
            url = url.replaceFirst(host, config.getProxyHostName());
            if (uri.getScheme().equals("https")) {
              url = url.replaceFirst("https", "http");
              base.setHeader("Scheme", "https");
            } else {
              base.setHeader("Scheme", "http");
            }
            base.setURI(URI.create(url));
            base.setHeader("Host", host);
          };

  @Before
  public void init() {
    httpClient = HttpClients.createDefault();
  }

  @Test(expected = HttpClientRuntimeException.class)
  public void test1() {
    HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(true, false)
            .setHost("www.baid?u.com");
    HttpResponseBuilder<String> responseBuilder = new HttpResponseBuilder<>(
            BeanCacheUtils.getBean(SimpleStringBodyDecoder.class),
            String.class
    );
    HttpClientResponse<String> response = new HttpClientEngine<>(
            httpClient,
            httpRequestBuilder,
            responseBuilder)
            .requireLog()
            .execute();
  }

  @Test
  public void test2() {
    HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(true, false)
            .setHost("www.baidu.com")
            .setProxy(
                    YOUZAN_QA_PROXY,
                    YOUZAN_DEFAULT_PROXY_BINDER
            );
    HttpResponseBuilder<String> responseBuilder = new HttpResponseBuilder<>(
            BeanCacheUtils.getBean(SimpleStringBodyDecoder.class),
            String.class
    );
    HttpClientResponse<String> response = new HttpClientEngine<>(
            httpClient,
            httpRequestBuilder,
            responseBuilder)
            .requireLog()
            .execute();
  }
}
