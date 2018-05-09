package com.zlatan.easy.httpclient.core.util;

import com.zlatan.easy.httpclient.core.exception.runtime.HttpClientRuntimeException;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.Callable;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 07/05/2018 3:50 PM
 */
public final class ArgValidationUtils {

  public static void validateStringNotBlank(final String fieldName, final String string) {
    if (StringUtils.isBlank(string)) {
      throw new HttpClientRuntimeException(String.format("Field %s cannot be blank.", fieldName));
    }
  }

  public static void validateObjectNotNull(final String fieldName, final Object object) {
    if (object == null) {
      throw new HttpClientRuntimeException(String.format("Field %s cannot be null.", fieldName));
    }
  }

  public static void validate(boolean expression, String message) {
    if (expression) {
      throw new HttpClientRuntimeException(message);
    }
  }

  public static void validate(Callable<Boolean> validation,
                              String message) {
    boolean exception;
    try {
      exception = validation.call();
    } catch (Exception e) {
      throw new HttpClientRuntimeException(message, e);
    }
    if (exception) {
      throw new HttpClientRuntimeException(message);
    }
  }
}
