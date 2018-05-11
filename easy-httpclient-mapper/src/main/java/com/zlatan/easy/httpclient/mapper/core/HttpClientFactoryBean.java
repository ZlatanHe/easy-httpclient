package com.zlatan.easy.httpclient.mapper.core;

import com.zlatan.easy.httpclient.mapper.configuration.EasyHttpConfiguration;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.FactoryBean;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 6:00 PM
 */
public class HttpClientFactoryBean implements FactoryBean<CloseableHttpClient> {

  private EasyHttpConfiguration easyHttpConfiguration;

  public HttpClientFactoryBean(EasyHttpConfiguration easyHttpConfiguration) {
    this.easyHttpConfiguration = easyHttpConfiguration;
  }

  @Override
  public final CloseableHttpClient getObject() {
    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    connectionManager.setDefaultMaxPerRoute(easyHttpConfiguration.getConnection().getMaxTotal());
    connectionManager.setMaxTotal(easyHttpConfiguration.getConnection().getDefaultMaxPerRoute());

    ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .build();

    SocketConfig socketConfig = SocketConfig.custom()
            .setBacklogSize(easyHttpConfiguration.getSocket().getBacklog())
            .setSoKeepAlive(easyHttpConfiguration.getSocket().isKeepAlive())
            .setSoTimeout(easyHttpConfiguration.getSocket().getSoTimeoutMs())
            .build();

    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(easyHttpConfiguration.getDefaultRequest().getConnectionTimeoutMs())
            .setSocketTimeout(easyHttpConfiguration.getDefaultRequest().getSocketTimeoutMs())
            .build();

    HttpClientBuilder builder = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultConnectionConfig(connectionConfig)
            .setDefaultSocketConfig(socketConfig)
            .setDefaultRequestConfig(requestConfig);

    return builder.build();
  }

  @Override
  public final Class<?> getObjectType() {
    return CloseableHttpClient.class;
  }

  @Override
  public final boolean isSingleton() {
    return true;
  }
}
