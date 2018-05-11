package com.zlatan.easy.httpclient.mapper.annotations.element;

import java.lang.annotation.*;

/**
 * @Title: Static header
 * @Description: Only support static http [k:v] headers such as
 * "Content-Type: application/x-www-form-urlencoded; charset=utf-8"
 *
 * @Date: Created By hewei in 09/05/2018 6:44 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface StaticHeaders {

  String[] headers() default {};
}
