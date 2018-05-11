package com.zlatan.easy.httpclient.mapper.core.template;

import com.zlatan.easy.httpclient.core.request.HttpRequestBuilder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 12:11 PM
 */
@Getter
public final class ModeledInterfaceTemplate extends InterfaceTemplate {

  private boolean useSSL;

  private String hostTemplate;

  private List<Identifier> hostIdentifiers;

  private String pathTemplate;

  private List<Identifier> pathIdentifiers;

  public ModeledInterfaceTemplate(boolean isGet) {
    super(isGet);
  }

  public ModeledInterfaceTemplate setUseSSL(boolean useSSL) {
    this.useSSL = useSSL;
    return this;
  }

  public ModeledInterfaceTemplate setHostTemplate(String hostTemplate) {
    this.hostTemplate = hostTemplate;
    return this;
  }

  public ModeledInterfaceTemplate setHostIdentifiers(List<Identifier> hostIdentifiers) {
    this.hostIdentifiers = hostIdentifiers;
    return this;
  }

  public ModeledInterfaceTemplate setPathTemplate(String pathTemplate) {
    this.pathTemplate = pathTemplate;
    return this;
  }

  public ModeledInterfaceTemplate setPathIdentifiers(List<Identifier> pathIdentifiers) {
    this.pathIdentifiers = pathIdentifiers;
    return this;
  }

  @Override
  public HttpRequestBuilder injectBasicConfig(Map<String, Object> runtimeConfigValues) {
    BiFunction<String, List<Identifier>, String> urlElementConstructor =
            (template, identifiers) ->
    {
      String[] values = new String[identifiers.size()];
      for (int i = 0; i < identifiers.size(); i++) {
        values[i] = runtimeConfigValues
                .computeIfAbsent(identifiers.get(i).getKey(), k -> "")
                .toString();
      }
      return String.format(template, values);
    };
    String host = urlElementConstructor.apply(hostTemplate, hostIdentifiers);
    String path = urlElementConstructor.apply(pathTemplate, pathIdentifiers);
    if (host.endsWith("/") && path.startsWith("/")) {
      host = host.substring(0, host.length() - 1);
    } else if (!host.endsWith("/") && !path.startsWith("/")) {
      host = host + "/";
    }
    String address = host + path;
    HttpRequestBuilder requestBuilder = new HttpRequestBuilder(isGet(), isUseSSL())
            .setHost(address);
    return requestBuilder;
  }
}
