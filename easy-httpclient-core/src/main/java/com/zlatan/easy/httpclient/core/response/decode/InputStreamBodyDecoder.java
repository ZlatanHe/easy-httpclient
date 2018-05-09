package com.zlatan.easy.httpclient.core.response.decode;

import org.apache.http.HttpEntity;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 08/05/2018 10:07 PM
 */
@SuppressWarnings("unchecked")
public class InputStreamBodyDecoder implements ResponseBodyDecoder {

  @Override
  public <T> T decode(HttpEntity entity, Class<? extends T> returnClass) throws Exception {
    return (T) entity.getContent();
  }
}
