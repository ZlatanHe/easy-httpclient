package com.zlatan.easy.httpclient.mapper.core.template;

import lombok.Getter;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 7:09 PM
 */
@Getter
public final class RequestConfigItem {

  private final ItemType itemType;

  private final String key;

  /**
   * Include wrapped class of primitive types
   * such as {@link Integer}
   *
   */
  private final boolean isPrimitive;

  private final boolean urlEncodeRequired;

  private final String charsetName;

  private final Method readMethod;

  private final List<RequestConfigItem> properties;

  public RequestConfigItem(ItemType itemType,
                           String key,
                           boolean isPrimitive,
                           boolean urlEncodeRequired,
                           String charsetName,
                           Method readMethod,
                           List<RequestConfigItem> properties) {
    this.itemType = itemType;
    this.key = key;
    this.isPrimitive = isPrimitive;
    this.urlEncodeRequired = urlEncodeRequired;
    this.charsetName = charsetName;
    this.readMethod = readMethod;
    this.properties = properties;
  }

  public static RequestConfigItem buildEntityConfig(List<RequestConfigItem> properties) {
    return new RequestConfigItem(
            ItemType.ENTITY,
            "",
            false,
            false,
            "",
            null,
            properties
    );
  }

  public static RequestConfigItem buildNormalConfig(ItemType itemType,
                                                    String key,
                                                    boolean isPrimitive,
                                                    boolean urlEncodeRequired,
                                                    String charsetName,
                                                    Method readMethod) {
    return new RequestConfigItem(
            itemType,
            key,
            isPrimitive,
            urlEncodeRequired,
            charsetName,
            readMethod,
            null
    );
  }

  public enum ItemType {

    URL,
    PATH,
    QUERY,
    BODY,
    HOST,
    HEADER,
    ENTITY
  }
}
