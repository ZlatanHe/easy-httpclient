package com.zlatan.easy.httpclient.mapper.annotations.element;

import java.lang.annotation.*;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 10:06 AM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface DynamicHeader {

  String value() default "";
}
