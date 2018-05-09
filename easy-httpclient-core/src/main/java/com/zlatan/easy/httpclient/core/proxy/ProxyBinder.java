package com.zlatan.easy.httpclient.core.proxy;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 07/05/2018 4:51 PM
 */
public interface ProxyBinder {

  void bind(ProxyConfig proxyConfig,
            HttpRequestBase httpRequestBase) throws Exception;
}
