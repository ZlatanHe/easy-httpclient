package com.zlatan.easy.httpclient.mapper.core.template;

import com.zlatan.easy.httpclient.core.exception.runtime.HttpClientRequestBuildException;
import com.zlatan.easy.httpclient.core.proxy.ProxyBinder;
import com.zlatan.easy.httpclient.core.proxy.ProxyConfig;
import com.zlatan.easy.httpclient.core.request.HttpRequestBuilder;
import com.zlatan.easy.httpclient.core.request.encode.RequestBodyEncoder;
import com.zlatan.easy.httpclient.core.response.HttpResponseBuilder;
import com.zlatan.easy.httpclient.core.response.decode.ResponseBodyDecoder;
import com.zlatan.easy.httpclient.core.util.ArgValidationUtils;
import com.zlatan.easy.httpclient.core.util.BeanCacheUtils;
import com.zlatan.easy.httpclient.core.util.JacksonUtils;
import com.zlatan.easy.httpclient.mapper.annotations.ForwardProxy;
import com.zlatan.easy.httpclient.mapper.configuration.EasyHttpConfiguration;
import com.zlatan.easy.httpclient.mapper.exception.HttpClientMapperBuildException;
import com.zlatan.easy.httpclient.mapper.util.BiConsumer;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 11:51 AM
 */
@Getter
@SuppressWarnings("unchecked")
public abstract class InterfaceTemplate {

  private final boolean isGet;

  private Class<? extends RequestBodyEncoder> requestBodyEncoderClass;

  private Class<? extends ResponseBodyDecoder> responseBodyDecoderClass;

  private Class returnClass;

  private Map<String, String> staticHeaders;

  private boolean proxyRequired;

  private String proxyPath = "";

  private int proxyPort = ProxyConfig.DEFAULT_PORT;

  private String proxyScheme = ProxyConfig.HTTP_SCHEME;

  private Class<? extends ProxyBinder> proxyBinderClass;

  private EasyHttpConfiguration.ProxyConfiguration globalProxyConfig;

  /**
   * Cache the item config list by method-defined order.
   *
   */
  private List<RequestConfigItem> itemConfigList;

  InterfaceTemplate(boolean isGet) {
    this.isGet = isGet;
  }

  public InterfaceTemplate setRequestBodyEncoderClass(Class<? extends RequestBodyEncoder> requestBodyEncoderClass) {
    this.requestBodyEncoderClass = requestBodyEncoderClass;
    return this;
  }

  public InterfaceTemplate setResponseBodyDecoderClass(Class<? extends ResponseBodyDecoder> responseBodyDecoderClass) {
    this.responseBodyDecoderClass = responseBodyDecoderClass;
    return this;
  }

  public InterfaceTemplate setReturnClass(Class returnClass) {
    this.returnClass = returnClass;
    return this;
  }

  public InterfaceTemplate setStaticHeaders(Map<String, String> staticHeaders) {
    this.staticHeaders = staticHeaders;
    return this;
  }

  public InterfaceTemplate setItemConfigList(List<RequestConfigItem> itemConfigList) {
    this.itemConfigList = itemConfigList;
    return this;
  }

  public InterfaceTemplate setGlobalProxyConfig(EasyHttpConfiguration.ProxyConfiguration globalProxyConfig) {
    this.globalProxyConfig = globalProxyConfig;
    return this;
  }

  public InterfaceTemplate checkProxyRequirement(Method method) {
    this.proxyRequired = method.isAnnotationPresent(ForwardProxy.class);
    if (proxyRequired) {
      ForwardProxy forwardProxy = method.getAnnotation(ForwardProxy.class);
      this.proxyPath = forwardProxy.value();
      this.proxyScheme = forwardProxy.useSSL() ? ProxyConfig.HTTPS_SCHEME : ProxyConfig.HTTP_SCHEME;
      ArgValidationUtils.validate(
              proxyPath.startsWith("http"),
              "Proxy path is not allowed to start with proxyScheme"
      );
      this.proxyPort = forwardProxy.port() > 0 ? forwardProxy.port() : ProxyConfig.DEFAULT_PORT;
      this.proxyBinderClass = forwardProxy.binder();
    }
    return this;
  }

  /**
   * Construct a {@link HttpRequestBuilder} instance by runtime arg values
   *
   * @param args runtime arg values
   */
  public HttpRequestBuilder constructRequestBuilder(Object[] args)
          throws HttpClientMapperBuildException {
    Map<String, Object> runtimeArgValues;
    try {
      runtimeArgValues = extractRuntimeConfigValues(args);
    } catch (Exception e) {
      throw new HttpClientMapperBuildException(e);
    }
    HttpRequestBuilder builder = injectBasicConfig(runtimeArgValues);
    addProxy(builder);
    if (!CollectionUtils.isEmpty(getStaticHeaders())) {
      getStaticHeaders().forEach((key, value) -> builder.addHeader(key, value));
    }
    return injectRequestElements(builder, runtimeArgValues);
  }

  public HttpResponseBuilder constructResponseBuilder() {
    return new HttpResponseBuilder(BeanCacheUtils.getBean(getResponseBodyDecoderClass()), getReturnClass());
  }

  /**
   * Construct {@link HttpRequestBuilder} with basic config.
   *
   * @see HttpRequestBuilder#HttpRequestBuilder(boolean, boolean)
   * @see HttpRequestBuilder#setHost(String)
   * @return
   */
  protected abstract HttpRequestBuilder injectBasicConfig(Map<String, Object> runtimeArgValues);

  /**
   * Pair up runtime values with request config items.
   * Necessary urlEncode will be performed.
   *
   * @see RequestConfigItem
   * @see RequestConfigItem#isUrlEncodeRequired()
   * @param args method args
   */
  private Map<String, Object> extractRuntimeConfigValues(Object[] args) throws Exception {
    Map<String, Object> runtimeArgValues = new HashMap<>();
    if (args != null) {
      BiConsumer<Object, RequestConfigItem> extract = (arg, itemConfig) -> {
        if (arg != null && itemConfig.isUrlEncodeRequired()) {
          try {
            arg = URLEncoder.encode(
                    itemConfig.isPrimitive() ? arg.toString() : JacksonUtils.toJson(arg),
                    itemConfig.getCharsetName()
            );
          } catch (UnsupportedEncodingException e) {
            throw new HttpClientRequestBuildException(e);
          }
        }
        runtimeArgValues.put(itemConfig.getKey(), arg);
      };
      for (int i = 0; i < args.length; i++) {
        Object arg = args[i];
        RequestConfigItem itemConfig = itemConfigList.get(i);
        if (RequestConfigItem.ItemType.ENTITY.equals(itemConfig.getItemType()) &&
                !CollectionUtils.isEmpty(itemConfig.getProperties())) {
          for (RequestConfigItem property : itemConfig.getProperties()) {
            Object value = property.getReadMethod().invoke(arg);
            extract.accept(value, property);
          }
        } else {
          extract.accept(arg, itemConfig);
        }
      }
    }
    return runtimeArgValues;
  }

  /**
   * Inject request elements into {@link HttpRequestBuilder}
   *
   * @see HttpRequestBuilder#addHeader(String, String)
   * @see HttpRequestBuilder#addQueryParam(String, String)
   * @see HttpRequestBuilder#addBodyParameter(String, Object)
   */
  private HttpRequestBuilder injectRequestElements(HttpRequestBuilder requestBuilder,
                                                   Map<String, Object> runtimeArgValues) {
    Consumer<RequestConfigItem> inject = configItem -> {
      String key = configItem.getKey();
      Object value = runtimeArgValues.computeIfAbsent(key, k -> "");
      switch (configItem.getItemType()) {
        case HEADER:
          requestBuilder.addHeader(key, value.toString());
          break;
        case QUERY:
          requestBuilder.addQueryParam(key, value.toString());
          break;
        case BODY:
          requestBuilder.addBodyParameter(key, value);
          break;
        default:
          break;
      }
    };
    for (RequestConfigItem configItem : getItemConfigList()) {
      if (RequestConfigItem.ItemType.ENTITY.equals(configItem.getItemType()) &&
              !CollectionUtils.isEmpty(configItem.getProperties())) {
        configItem.getProperties().forEach(property -> inject.accept(property));
      } else {
        inject.accept(configItem);
      }
    }
    return requestBuilder;
  }

  /**
   * Add proxy to request
   *
   */
  private void addProxy(HttpRequestBuilder requestBuilder) {
    do {
      if (!globalProxyConfig.isEnabled()) {
        break;
      }
      if (!proxyRequired) {
        break;
      }
      if (StringUtils.isBlank(proxyPath)) {
        String proxyUrl = globalProxyConfig.getDefaultProxyUrl();
        int protocolEnd = proxyUrl.indexOf("://");
        proxyScheme = proxyUrl.substring(0, protocolEnd);
        proxyPath = proxyUrl.substring(protocolEnd + 3, proxyUrl.length());
        proxyPort = globalProxyConfig.getDefaultPort() > 0
                ? globalProxyConfig.getDefaultPort()
                : ProxyConfig.DEFAULT_PORT;
      }
      ProxyConfig proxyConfig = new ProxyConfig(proxyPath, proxyPort, proxyScheme);
      requestBuilder.setProxy(proxyConfig, BeanCacheUtils.getBean(proxyBinderClass));
    } while (false);
  }
}
