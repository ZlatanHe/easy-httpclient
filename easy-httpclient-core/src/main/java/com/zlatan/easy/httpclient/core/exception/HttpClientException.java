package com.zlatan.easy.httpclient.core.exception;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 07/05/2018 3:28 PM
 */
public class HttpClientException extends Exception {

  public HttpClientException() {
  }

  public HttpClientException(String message) {
    super(message);
  }

  public HttpClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpClientException(Throwable cause) {
    super(cause);
  }

  public HttpClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
