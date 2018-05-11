package com.zlatan.easy.httpclient.mapper.annotations;

import com.zlatan.easy.httpclient.core.proxy.ProxyBinder;
import com.zlatan.easy.httpclient.core.proxy.ProxyConfig;
import com.zlatan.easy.httpclient.mapper.support.SimpleHttpProxyBinder;
import org.apache.http.client.methods.HttpRequestBase;

import java.lang.annotation.*;

/**
 * @Title:
 * @Description: Annotation which represents the requirement of forward proxy.
 * Proxy host and proxy binder are also provided to set a proxy for a http request.
 *
 * @Date: Created By hewei in 11/05/2018 7:46 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ForwardProxy {

  /**
   * Host of proxy service
   * e.g. proxy-qa.s.qima-inc.com
   *
   * @see ProxyConfig#getProxyHostName()
   */
  String value() default "";

  /**
   * Port of proxy service
   *
   */
  int port() default -1;

  /**
   * If true, access the proxy service via https;
   * otherwise http.
   *
   */
  boolean useSSL() default false;

  /**
   * class of an implementation of {@link ProxyBinder}
   *
   * @see ProxyBinder#bind(ProxyConfig, HttpRequestBase)
   */
  Class<? extends ProxyBinder> binder() default SimpleHttpProxyBinder.class;
}
