package com.zlatan.easy.httpclient.mapper.annotations;

import com.zlatan.easy.httpclient.core.request.encode.RequestBodyEncoder;
import com.zlatan.easy.httpclient.core.request.encode.SimpleJsonRequestBodyEncoder;
import com.zlatan.easy.httpclient.mapper.annotations.config.HttpRequestConfiguration;
import com.zlatan.easy.httpclient.mapper.annotations.element.Host;
import com.zlatan.easy.httpclient.mapper.annotations.element.Path;
import com.zlatan.easy.httpclient.mapper.constant.RequestMethod;

import java.lang.annotation.*;

/**
 * @Title: Metadata which defines a request template.
 * @Description:
 * URI = scheme:[//authority]path[?query][#fragment]
 * 1. Full authority pattern is not supported. Instead, only hostname
 * and ip:port pair are supported.
 * 2. Fragment is not supported. Question mark will only be regarded
 * as the beginning of a query pattern.
 *
 * @Date: Created By hewei in 09/05/2018 4:33 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface HttpRequest {

  /**
   * Http Reqeust Method
   * For more info, go to {@link RequestMethod}.
   *
   */
  RequestMethod method() default RequestMethod.GET;

  /**
   * Set the encoder which encodes the body params
   * if the request has a body.
   *
   */
  Class<? extends RequestBodyEncoder> encoder() default SimpleJsonRequestBodyEncoder.class;

  /**
   * If true, the scheme is https; else the scheme is http.
   *
   */
  boolean useSSL() default false;

  /**
   * Host of the request.
   * For example, www.baidu.com or an ip:port pair like 10.147.12.1:8081.
   *
   * @see Host
   */
  String host();

  /**
   * path. For example, /user/queryUserInfo
   *
   * @see Path
   */
  String path() default "";

  /**
   * request configurations.
   *
   * @see HttpRequestConfiguration
   */
  HttpRequestConfiguration configuration() default @HttpRequestConfiguration;
}
