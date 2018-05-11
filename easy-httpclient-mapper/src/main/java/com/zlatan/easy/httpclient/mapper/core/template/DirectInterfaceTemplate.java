package com.zlatan.easy.httpclient.mapper.core.template;

import com.zlatan.easy.httpclient.core.request.HttpRequestBuilder;
import lombok.Getter;

import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 12:11 PM
 */
@Getter
public final class DirectInterfaceTemplate extends InterfaceTemplate {

  private String urlKey;

  private boolean dynamic;

  public DirectInterfaceTemplate(boolean isGet) {
    super(isGet);
  }

  public DirectInterfaceTemplate setUrlkey(String urlKey) {
    this.urlKey = urlKey;
    return this;
  }

  public DirectInterfaceTemplate setDynamic(boolean dynamic) {
    this.dynamic = dynamic;
    return this;
  }

  @Override
  public HttpRequestBuilder injectBasicConfig(Map<String, Object> runtimeConfigValues) {
    String url;
    if (dynamic) {
      url = runtimeConfigValues.computeIfAbsent(urlKey, k -> "").toString();
    } else {
      url = urlKey;
    }
    if (!url.startsWith("http")) {
      url = "http://" + url;
    }
    int protocolEnd = url.indexOf("://");
    url = url.substring(protocolEnd + 3);
    HttpRequestBuilder builder = new HttpRequestBuilder(isGet(), url.startsWith("https://"))
            .setHost(url);
    return builder;
  }
}
