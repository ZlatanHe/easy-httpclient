package com.zlatan.easy.httpclient.mapper.exception;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 11:09 AM
 */
public class HttpClientMapperBuildException extends Exception {

  public HttpClientMapperBuildException(String message) {
    super(message);
  }

  public HttpClientMapperBuildException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpClientMapperBuildException(Throwable cause) {
    super(cause);
  }
}
