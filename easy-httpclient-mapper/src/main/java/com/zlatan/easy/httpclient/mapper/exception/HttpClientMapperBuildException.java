package com.zlatan.easy.httpclient.mapper.exception;

import com.zlatan.easy.httpclient.core.exception.HttpClientException;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 11:09 AM
 */
public class HttpClientMapperBuildException extends HttpClientException {

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
