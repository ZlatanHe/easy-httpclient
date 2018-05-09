package com.zlatan.easy.httpclient.core.request.encode;

import com.zlatan.easy.httpclient.core.util.JacksonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 07/05/2018 9:18 PM
 */
public class SimpleJsonRequestBodyEncoder implements RequestBodyEncoder {

  @Override
  public HttpEntity encode(Map<String, Object> parameters) throws Exception {
    String value = JacksonUtils.toJson(parameters);
    return new StringEntity(value, StandardCharsets.UTF_8.displayName());
  }
}
