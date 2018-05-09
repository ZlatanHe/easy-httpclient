package com.zlatan.easy.httpclient.core.exception;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 08/05/2018 2:27 PM
 */
public class HttpClientDeserializationException extends HttpClientException {

  public HttpClientDeserializationException(String message) {
    super(message);
  }

  public HttpClientDeserializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpClientDeserializationException(Throwable cause) {
    super(cause);
  }
}
