package com.zlatan.easy.httpclient.core.exception.runtime;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 2:30 PM
 */
public class HttpClientExecutionException extends HttpClientRuntimeException {

  public HttpClientExecutionException(String message) {
    super(message);
  }

  public HttpClientExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpClientExecutionException(Throwable cause) {
    super(cause);
  }
}
