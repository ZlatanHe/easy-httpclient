package com.zlatan.easy.httpclient.mapper.scan;

import com.zlatan.easy.httpclient.mapper.configuration.EasyHttpConfiguration;
import com.zlatan.easy.httpclient.mapper.core.HttpClientMapper;
import com.zlatan.easy.httpclient.mapper.core.HttpClientMapperBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 5:23 PM
 */
@SuppressWarnings("unchecked")
public class HttpClientMapperFactoryBean<T> implements FactoryBean<T> {

  private final Class<T> interfaceClass;

  private CloseableHttpClient httpClient;

  private EasyHttpConfiguration easyHttpConfiguration;

  private final Map<Method, HttpClientMapper> methodBeanCache;

  public HttpClientMapperFactoryBean(Class<T> interfaceClass) {
    this.interfaceClass = interfaceClass;
    this.methodBeanCache = new IdentityHashMap<>();
  }

  public void setHttpClient(CloseableHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public void setEasyHttpConfiguration(EasyHttpConfiguration easyHttpConfiguration) {
    this.easyHttpConfiguration = easyHttpConfiguration;
  }

  @Override
  public T getObject() throws Exception {
    return (T) Proxy.newProxyInstance(
            interfaceClass.getClassLoader(),
            new Class<?>[]{interfaceClass},
            (proxy, method, args) -> {
              if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
              }
              HttpClientMapper<?> methodBean = (HttpClientMapper<?>) methodBeanCache.get(method);
              if (methodBean == null) {
                synchronized (method) {
                  methodBean = methodBeanCache.get(method);
                  if (methodBean == null) {
                    methodBean = new HttpClientMapperBuilder<>(
                            httpClient,
                            easyHttpConfiguration.getProxy(),
                            method
                    ).build();
                    methodBeanCache.put(method, methodBean);
                  }
                }
              }
              return methodBean.invoke(args);
            }
    );
  }

  @Override
  public Class<?> getObjectType() {
    return interfaceClass;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
