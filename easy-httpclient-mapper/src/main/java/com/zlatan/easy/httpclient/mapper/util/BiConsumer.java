package com.zlatan.easy.httpclient.mapper.util;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 11/05/2018 11:21 AM
 */
public interface BiConsumer<T, U> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t the first input argument
   * @param u the second input argument
   */
  void accept(T t, U u) throws Exception;
}
