package com.zlatan.easy.httpclient.mapper.annotations.element;

import com.zlatan.easy.httpclient.mapper.annotations.HttpRequest;

import java.lang.annotation.*;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 6:15 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Documented
public @interface RequestBody {

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
}
