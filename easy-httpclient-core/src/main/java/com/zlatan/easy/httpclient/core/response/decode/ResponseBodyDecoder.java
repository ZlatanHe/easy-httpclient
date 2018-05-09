package com.zlatan.easy.httpclient.core.response.decode;

import com.zlatan.easy.httpclient.core.exception.runtime.HttpClientResponseBuildException;
import org.apache.http.HttpEntity;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 08/05/2018 9:33 PM
 */
public interface ResponseBodyDecoder {

  <T> T decode(HttpEntity entity, Class<? extends T> returnClass) throws Exception;

  default <T> T invoke(HttpEntity entity, Class<? extends T> returnClass) throws HttpClientResponseBuildException {
    try {
      return decode(entity, returnClass);
    } catch (Exception e) {
      throw new HttpClientResponseBuildException(e);
    }
  }
}
