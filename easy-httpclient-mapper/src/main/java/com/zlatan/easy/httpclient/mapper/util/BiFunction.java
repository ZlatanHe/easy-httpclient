package com.zlatan.easy.httpclient.mapper.util;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 11/05/2018 1:22 PM
 */
public interface BiFunction<T, U, R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param t the first function argument
   * @param u the second function argument
   * @return the function result
   */
  R apply(T t, U u) throws Exception;
}