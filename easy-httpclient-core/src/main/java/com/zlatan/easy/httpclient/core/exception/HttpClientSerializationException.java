package com.zlatan.easy.httpclient.core.exception;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 08/05/2018 2:25 PM
 */
public class HttpClientSerializationException extends HttpClientException {

  public HttpClientSerializationException(String message) {
    super(message);
  }

  public HttpClientSerializationException(Throwable cause) {
    super(cause);
  }

  public HttpClientSerializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
