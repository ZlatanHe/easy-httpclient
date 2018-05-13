package com.zlatan.easy.httpclient.core.request;

import com.zlatan.easy.httpclient.core.exception.HttpClientRequestBuildException;
import com.zlatan.easy.httpclient.core.proxy.ProxyBinder;
import com.zlatan.easy.httpclient.core.proxy.ProxyConfig;
import com.zlatan.easy.httpclient.core.request.encode.RequestBodyEncoder;
import com.zlatan.easy.httpclient.core.request.encode.SimpleJsonRequestBodyEncoder;
import com.zlatan.easy.httpclient.core.util.ArgValidationUtils;
import com.zlatan.easy.httpclient.core.util.BeanCacheUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 07/05/2018 3:22 PM
 */
public final class HttpRequestBuilder {

  private final boolean isGet;

  private final boolean useSSL;

  private volatile boolean proxyBound;

  private String host;

  private Map<String, String> queryParams;

  private Map<String, String> headers;

  private Map<String, Object> postBodyParams;

  private RequestBodyEncoder requestBodyEncoder;

  private ProxyConfig proxyConfig;

  private ProxyBinder proxyBinder;

  public HttpRequestBuilder(final boolean isGet,
                            final boolean useSSL) {
    this.isGet = isGet;
    this.useSSL = useSSL;
    this.proxyBound = false;
    this.queryParams = new HashMap<>();
    this.headers = new HashMap<>();
    if (!isGet) {
      this.postBodyParams = new HashMap<>();
      this.requestBodyEncoder = BeanCacheUtils.getBean(SimpleJsonRequestBodyEncoder.class);
    }
  }

  public HttpGet buildGet() {
    if (!isGet) {
      throw new HttpClientRequestBuildException("A Post builder cannot build Get request.", new ClassCastException());
    }
    return (HttpGet) build();
  }

  public HttpPost buildPost() {
    if (isGet) {
      throw new HttpClientRequestBuildException("A Get builder cannot build Post request.", new ClassCastException());
    }
    return (HttpPost) build();
  }

  public HttpRequestBase build() {
    ArgValidationUtils.validateStringNotBlank("host", host);
    final StringBuilder sb = useSSL ? new StringBuilder("https://") : new StringBuilder("http://");
    sb.append(host);
    if (!queryParams.isEmpty()) {
      if (sb.indexOf("?") == -1) {
        sb.append("?");
      }
      for (Map.Entry<String, String> entry : queryParams.entrySet()) {
        try {
          sb.append(String.format(
                  "%s=%s",
                  entry.getKey(),
                  URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.displayName())
          ));
        } catch (Exception e) {
          throw new HttpClientRequestBuildException("Query item append was failed.", e);
        }
      }
    }
    String url = sb.toString();
    HttpRequestBase base = isGet ? new HttpGet(url) : new HttpPost(url);
    headers.forEach((key, value) -> base.setHeader(key, value));
    if (!isGet && !postBodyParams.isEmpty()) {
      HttpPost httpPost = (HttpPost) base;
      httpPost.setEntity(requestBodyEncoder.invoke(postBodyParams));
    }
    if (proxyBound) {
      try {
        proxyBinder.bind(proxyConfig, base);
      } catch (Exception e) {
        throw new HttpClientRequestBuildException(e);
      }
    }
    return base;
  }

  public HttpRequestBuilder setHost(final String host) {
    ArgValidationUtils.validateStringNotBlank("host", host);
    ArgValidationUtils.validate(
            () -> {
              boolean startWithProtocol = host.startsWith("http");
              boolean hasQuery = host.contains("?");
              return startWithProtocol || hasQuery;
            },
            String.format("Invalid host: %s", host)
    );
    this.host = host;
    return this;
  }

  public HttpRequestBuilder addQueryParam(final String key, final String value) {
    ArgValidationUtils.validateStringNotBlank("queryKey", key);
    ArgValidationUtils.validateStringNotBlank("queryValue", value);
    queryParams.put(key, value);
    return this;
  }

  public HttpRequestBuilder addHeader(final String key,
                                      final String value) {
    ArgValidationUtils.validateStringNotBlank("headerKey", key);
    ArgValidationUtils.validateStringNotBlank("headerValue", value);
    headers.put(key, value);
    return this;
  }

  public HttpRequestBuilder setProxy(final ProxyConfig proxyConfig,
                                     final ProxyBinder proxyBinder) {
    ArgValidationUtils.validateObjectNotNull("proxyConfig", proxyConfig);
    ArgValidationUtils.validateObjectNotNull("proxyBinder", proxyBinder);
    if (proxyBound) {
      throw new HttpClientRequestBuildException("Proxy is repeatedly bound.");
    }
    this.proxyConfig = proxyConfig;
    this.proxyBinder = proxyBinder;
    this.proxyBound = true;
    return this;
  }

  public HttpRequestBuilder setRequestBodyEncoder(RequestBodyEncoder requestBodyEncoder) {
    ArgValidationUtils.validateObjectNotNull("requestBodyEncoder", requestBodyEncoder);
    this.requestBodyEncoder = requestBodyEncoder;
    return this;
  }

  public HttpRequestBuilder addBodyParameter(final String key, final Object bodyParam) {
    ArgValidationUtils.validateStringNotBlank("key", key);
    ArgValidationUtils.validateObjectNotNull("httpPostBodyParam", bodyParam);
    if (isGet) {
      throw new HttpClientRequestBuildException("Get request is not allowed to have body params.");
    }
    postBodyParams.put(key, bodyParam);
    return this;
  }
}
