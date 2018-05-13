package com.zlatan.easy.httpclient.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zlatan.easy.httpclient.core.exception.HttpClientDeserializationException;
import com.zlatan.easy.httpclient.core.exception.HttpClientSerializationException;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 07/05/2018 9:32 PM
 */
public final class JacksonUtils {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  static {
    // Deserialization configuration
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    // Serialization configuration
    OBJECT_MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    OBJECT_MAPPER.configure(MapperFeature.AUTO_DETECT_FIELDS, false);
    OBJECT_MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  public static String toJson(Object o) {
    try {
      return OBJECT_MAPPER.writeValueAsString(o);
    } catch (Exception e) {
      throw new HttpClientSerializationException(e);
    }
  }

  public static byte[] toBytes(Object o) {
    try {
      return OBJECT_MAPPER.writeValueAsBytes(o);
    } catch (Exception e) {
      throw new HttpClientSerializationException(e);
    }
  }

  public static <T> T toObject(String json, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (Exception e) {
      throw new HttpClientDeserializationException(e);
    }
  }
}
