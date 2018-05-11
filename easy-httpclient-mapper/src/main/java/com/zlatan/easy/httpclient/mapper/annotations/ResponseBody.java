package com.zlatan.easy.httpclient.mapper.annotations;

import com.zlatan.easy.httpclient.core.response.decode.ResponseBodyDecoder;
import com.zlatan.easy.httpclient.core.response.decode.SimpleStringBodyDecoder;

import java.lang.annotation.*;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 5:03 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ResponseBody {

  Class<? extends ResponseBodyDecoder> decoder() default SimpleStringBodyDecoder.class;

  Class<?> returnClass() default String.class;
}
