package com.zlatan.easy.httpclient.mapper.annotations.element;

import com.zlatan.easy.httpclient.mapper.annotations.HttpRequest;

import java.lang.annotation.*;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 6:14 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface Query {

  /**
   * The value which matches one of the identifier
   * in the value template. If the value is blank,
   * name of the parameter will be used to perform
   * the matching.
   * Better give a value since the parameter name
   * is not always present.
   *
   * @see HttpRequest
   */
  String value() default "";

  /**
   * Whether it is needed to preform urlEncode on
   * this item.
   *
   * @see java.net.URLEncoder#encode(String, String)
   */
  boolean urlEncodeRequired() default true;

  /**
   * The name of a supported
   * <a href="../lang/package-summary.html#charenc">character
   * encoding</a>.
   *
   * @see java.net.URLEncoder#encode(String, String)
   */
  String charset() default "UTF-8";
}
