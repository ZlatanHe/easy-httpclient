package com.zlatan.easy.httpclient.core.response.decode;

import com.zlatan.easy.httpclient.core.exception.HttpClientResponseBuildException;
import com.zlatan.easy.httpclient.core.util.JacksonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;

import java.nio.charset.StandardCharsets;

/**
 * @Title:
 * @Description: If developers use this decoder, they make sure
 * that the response body is the format of Json. Therefore, once the
 * response is not, it is acceptable that an Exception is thrown.
 *
 * @Date: Created By hewei in 08/05/2018 9:52 PM
 */
public class JacksonResponseBodyDecoder implements ResponseBodyDecoder {

  @Override
  public <T> T decode(HttpEntity entity, Class<? extends T> returnClass) throws Exception {
    String responseBody = IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
    T body;
    try {
      body = JacksonUtils.toObject(responseBody, returnClass);
    } catch (Exception e) {
      throw new HttpClientResponseBuildException(
              String.format("Jackson deserialization was failed. Response content = %s.", responseBody),
              e
      );
    }
    return body;
  }
}
