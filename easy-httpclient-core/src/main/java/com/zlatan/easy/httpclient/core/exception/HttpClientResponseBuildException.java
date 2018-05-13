package com.zlatan.easy.httpclient.core.exception;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 08/05/2018 9:45 PM
 */
public class HttpClientResponseBuildException extends HttpClientRuntimeException {

  public HttpClientResponseBuildException(String message) {
    super(message);
  }

  public HttpClientResponseBuildException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpClientResponseBuildException(Throwable cause) {
    super(cause);
  }
}
