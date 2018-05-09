package com.zlatan.easy.httpclient.core.request.encode;

import com.zlatan.easy.httpclient.core.util.JacksonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 08/05/2018 9:02 PM
 */
public class MultipartRequestBodyEncoder implements RequestBodyEncoder {

  @Override
  public HttpEntity encode(Map<String, Object> parameters) throws Exception {
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
      Object value = entry.getValue();
      if (value instanceof byte[]) {
        builder.addBinaryBody(entry.getKey(), (byte[]) value);
      } else {
        builder.addTextBody(entry.getKey(), JacksonUtils.toJson(value));
      }
    }
    return builder.build();
  }
}
