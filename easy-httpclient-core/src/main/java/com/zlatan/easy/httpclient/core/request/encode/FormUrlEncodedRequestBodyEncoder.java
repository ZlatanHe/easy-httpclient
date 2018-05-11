package com.zlatan.easy.httpclient.core.request.encode;

import com.zlatan.easy.httpclient.core.constant.TypeCache;
import com.zlatan.easy.httpclient.core.util.JacksonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 08/05/2018 8:17 PM
 */
public class FormUrlEncodedRequestBodyEncoder implements RequestBodyEncoder {

  @Override
  public HttpEntity encode(Map<String, Object> parameters) throws Exception {
    List<BasicNameValuePair> nameValuePairs = new ArrayList<>(parameters.size());
    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
      Object value = entry.getValue();
      String valueStr;
      if (TypeCache.fundamentalClassSet.contains(value.getClass())) {
        valueStr = value.toString();
      } else {
        valueStr = JacksonUtils.toJson(value);
      }
      nameValuePairs.add(new BasicNameValuePair(entry.getKey(), valueStr));
    }
    return new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
  }
}
