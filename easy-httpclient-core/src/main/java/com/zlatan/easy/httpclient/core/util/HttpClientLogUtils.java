package com.zlatan.easy.httpclient.core.util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.Closeable;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 07/05/2018 6:00 PM
 */
public final class HttpClientLogUtils {

  public static void printHttpRequestInfo(Logger logger,
                                          HttpRequestBase httpRequestBase) {
    ArgValidationUtils.validateObjectNotNull("logger", logger);
    StringBuilder sb = new StringBuilder("\n").append(httpRequestBase.toString()).append("\n");
    Header[] headers = httpRequestBase.getAllHeaders();
    for (Header header : headers) {
      sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
    }
    if (HttpPost.class.isInstance(httpRequestBase)) {
      try {
        HttpPost httpPost = (HttpPost) httpRequestBase;
        sb.append(EntityUtils.toString(httpPost.getEntity(), "UTF-8"));
      } catch (Exception e) {
        logger.warn("HttpPost Request Entity parsing was failed while logging.");
      }
    } else {
      sb.deleteCharAt(sb.length() - 1);
    }
    logger.info(sb.toString());
  }

  public static void printHttpResponseInfo(Logger logger,
                                           HttpResponse httpResponse,
                                           Object body) {
    StringBuilder sb = new StringBuilder("\n");
    sb.append(httpResponse.getStatusLine());
    sb.append("\n");
    for (Header header : httpResponse.getAllHeaders()) {
      sb.append(String.format("%s: %s", header.getName(), header.getValue()));
      sb.append("\n");
    }
    if (body == null || Closeable.class.isInstance(body)) {
      sb.deleteCharAt(sb.length() - 1);
    } else {
      try {
        sb.append(JacksonUtils.toJson(body));
      } catch (Exception e) {
        logger.warn("HttpResponseBody decode was failed while logging.");
      }
    }
    logger.info(sb.toString());
  }
}
