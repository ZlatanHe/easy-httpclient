package com.zlatan.easy.httpclient.mapper.annotations.config;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 13/05/2018 9:47 PM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface HttpRequestConfiguration {

  int connectTimeoutMs() default -1;

  int connectionRequestTimeoutMs() default -1;

  int socketTimeoutMs() default -1;
}
