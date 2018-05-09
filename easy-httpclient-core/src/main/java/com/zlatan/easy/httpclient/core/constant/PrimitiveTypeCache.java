package com.zlatan.easy.httpclient.core.constant;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 08/05/2018 8:42 PM
 */
public interface PrimitiveTypeCache {

  Set<Class> primitiveClassSet = Sets.newHashSet(
          String.class,
          int.class,
          Integer.class,
          long.class,
          Long.class,
          float.class,
          Float.class,
          double.class,
          Double.class,
          short.class,
          Short.class,
          boolean.class,
          Boolean.class,
          void.class,
          Void.class
  );
}
