package com.zlatan.easy.httpclient.mapper.annotations;

import com.zlatan.easy.httpclient.core.request.encode.RequestBodyEncoder;
import com.zlatan.easy.httpclient.core.request.encode.SimpleJsonRequestBodyEncoder;
import com.zlatan.easy.httpclient.mapper.annotations.element.Url;
import com.zlatan.easy.httpclient.mapper.constant.RequestMethod;

import java.lang.annotation.*;

/**
 * @Title:
 * @Description: The value in {@link #value()}, or runtime
 * values which matches the value in {@link #value()} will be
 * used directly as a URL(Unified Resource Locator).
 *
 * @Date: Created By hewei in 09/05/2018 8:45 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DirectUrlHttpRequest {

  /**
   * Http Reqeust Method
   * For more info, go to {@link RequestMethod}.
   *
   */
  RequestMethod method() default RequestMethod.GET;

  /**
   * A static value or a value paired with a param
   * of the request method.
   *
   */
  String value();

  /**
   * If true, the method must contain one param that
   * decorated by {@link Url}.
   */
  boolean dynamic() default false;

  /**
   * Set the encoder which encodes the body params
   * if the request has a body.
   *
   */
  Class<? extends RequestBodyEncoder> encoder() default SimpleJsonRequestBodyEncoder.class;
}

