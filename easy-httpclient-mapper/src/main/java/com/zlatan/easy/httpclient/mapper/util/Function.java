package com.zlatan.easy.httpclient.mapper.util;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 11/05/2018 1:16 PM
 */
public interface Function<T, R> {

  /**
   * Applies this function to the given argument.
   *
   * @param t the function argument
   * @return the function result
   */
  R apply(T t) throws Exception;
}
