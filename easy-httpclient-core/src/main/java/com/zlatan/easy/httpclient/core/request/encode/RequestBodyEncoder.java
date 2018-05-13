package com.zlatan.easy.httpclient.core.request.encode;

import com.zlatan.easy.httpclient.core.exception.HttpClientRequestBuildException;
import org.apache.http.HttpEntity;

import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 07/05/2018 5:42 PM
 */
public interface RequestBodyEncoder {

  HttpEntity encode(Map<String, Object> parameters) throws Exception;

  default HttpEntity invoke(Map<String, Object> parameters) throws HttpClientRequestBuildException {
    try {
      return encode(parameters);
    } catch (Exception e) {
      throw new HttpClientRequestBuildException(e);
    }
  }
}
