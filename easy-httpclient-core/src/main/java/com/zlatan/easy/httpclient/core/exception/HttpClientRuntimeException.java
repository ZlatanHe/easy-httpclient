package com.zlatan.easy.httpclient.core.exception;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 07/05/2018 3:31 PM
 */
public class HttpClientRuntimeException extends RuntimeException {

  public HttpClientRuntimeException() {
  }

  public HttpClientRuntimeException(String message) {
    super(message);
  }

  public HttpClientRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpClientRuntimeException(Throwable cause) {
    super(cause);
  }

  public HttpClientRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
