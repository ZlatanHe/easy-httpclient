package com.zlatan.easy.httpclient.core.util;

import lombok.extern.slf4j.Slf4j;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @Title: BeanCacheUtils
 * @Description: Cache beans in a map of which keys are the class objects and provide a method
 * {@link #getBean(Class)} for getting a bean by its class.
 * If the bean is not found, try to instantiate the bean by {@link Class#newInstance()}.
 * In fact this method invoke the non-arg constructor of the class. Therefore if the class has
 * no non-arg constructor, a {@link NoSuchMethodException} will be thrown and caught, then
 * null is returned.
 *
 * @Date: Created By hewei in 04/05/2018 7:24 PM
 */
@Slf4j
public final class BeanCacheUtils {

  private static final Map<Class, Object> beanCache = new IdentityHashMap<>();

  public static <T> T getBean(Class<T> beanClass) {
    T bean = (T) beanCache.get(beanClass);
    if (bean == null) {
      synchronized (beanCache) {
        bean = (T) beanCache.get(beanClass);
        if (bean == null) {
          try {
            bean = (T) beanCache.get(beanClass);
          } catch (Exception e) {
          }
        }
        if (bean == null) {
          try {
            bean = beanClass.newInstance();
            beanCache.put(beanClass, bean);
          } catch (Exception e) {
            log.error("Bean instantiation failed. class={}", beanClass, e);
          }
        }
      }
    }
    return bean;
  }
}
