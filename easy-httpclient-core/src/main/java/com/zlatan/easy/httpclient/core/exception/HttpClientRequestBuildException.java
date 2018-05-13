package com.zlatan.easy.httpclient.core.exception;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 07/05/2018 5:48 PM
 */
public class HttpClientRequestBuildException extends HttpClientRuntimeException {

  public HttpClientRequestBuildException(String message) {
    super(message);
  }

  public HttpClientRequestBuildException(Throwable cause) {
    super(cause);
  }

  public HttpClientRequestBuildException(String message, Throwable cause) {
    super(message, cause);
  }
}
