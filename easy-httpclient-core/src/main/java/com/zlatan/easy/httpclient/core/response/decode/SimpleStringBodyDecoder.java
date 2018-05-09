package com.zlatan.easy.httpclient.core.response.decode;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;

import java.nio.charset.StandardCharsets;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 08/05/2018 10:12 PM
 */
@SuppressWarnings("unchecked")
public class SimpleStringBodyDecoder implements ResponseBodyDecoder {

  @Override
  public <T> T decode(HttpEntity entity, Class<? extends T> returnClass) throws Exception {
    return (T) IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
  }
}
