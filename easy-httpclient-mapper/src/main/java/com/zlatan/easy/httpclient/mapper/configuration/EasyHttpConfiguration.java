package com.zlatan.easy.httpclient.mapper.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.util.List;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 6:52 PM
 */
@ConfigurationProperties("easy.httpClient")
public class EasyHttpConfiguration {

  private ConnectionConfiguration connection = new ConnectionConfiguration();

  private ProxyConfiguration proxy = new ProxyConfiguration();

  private List<CertificateConfiguration> certificates;

  private SocketConfiguration socket;

  private RequestConfiguration defaultRequest;

  public ConnectionConfiguration getConnection() {
    return connection;
  }

  public void setConnection(ConnectionConfiguration connection) {
    this.connection = connection;
  }

  public List<CertificateConfiguration> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<CertificateConfiguration> certificates) {
    this.certificates = certificates;
  }

  public SocketConfiguration getSocket() {
    return socket;
  }

  public void setSocket(SocketConfiguration socket) {
    this.socket = socket;
  }

  public RequestConfiguration getDefaultRequest() {
    return defaultRequest;
  }

  public void setDefaultRequest(RequestConfiguration defaultRequest) {
    this.defaultRequest = defaultRequest;
  }

  public ProxyConfiguration getProxy() {
    return proxy;
  }

  public void setProxy(ProxyConfiguration proxy) {
    this.proxy = proxy;
  }

  public static class SocketConfiguration {

    private boolean keepAlive = true;

    private int backlog = 512;

    private int soTimeoutMs = 2000;

    public boolean isKeepAlive() {
      return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
      this.keepAlive = keepAlive;
    }

    public int getBacklog() {
      return backlog;
    }

    public void setBacklog(int backlog) {
      this.backlog = backlog;
    }

    public int getSoTimeoutMs() {
      return soTimeoutMs;
    }

    public void setSoTimeoutMs(int soTimeoutMs) {
      this.soTimeoutMs = soTimeoutMs;
    }
  }

  public static class CertificateConfiguration {

    private String password;

    private String type;

    private File file;

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public File getFile() {
      return file;
    }

    public void setFile(File file) {
      this.file = file;
    }
  }

  public static class RequestConfiguration {

    private int connectionTimeoutMs = 2000;

    private int socketTimeoutMs = 2000;

    public int getConnectionTimeoutMs() {
      return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
      this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getSocketTimeoutMs() {
      return socketTimeoutMs;
    }

    public void setSocketTimeoutMs(int socketTimeoutMs) {
      this.socketTimeoutMs = socketTimeoutMs;
    }
  }

  public static class ConnectionConfiguration {

    private int maxTotal = 100000;

    private int defaultMaxPerRoute = 100000;

    public int getMaxTotal() {
      return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
      this.maxTotal = maxTotal;
    }

    public int getDefaultMaxPerRoute() {
      return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
      this.defaultMaxPerRoute = defaultMaxPerRoute;
    }
  }

  public static class ProxyConfiguration {

    private boolean enabled = false;

    private String defaultProxyUrl;

    private int defaultPort = -1;

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public String getDefaultProxyUrl() {
      return defaultProxyUrl;
    }

    public void setDefaultProxyUrl(String defaultProxyUrl) {
      this.defaultProxyUrl = defaultProxyUrl;
    }

    public int getDefaultPort() {
      return defaultPort;
    }

    public void setDefaultPort(int defaultPort) {
      this.defaultPort = defaultPort;
    }
  }
}
