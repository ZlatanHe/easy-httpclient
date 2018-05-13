package com.zlatan.easy.httpclient.core.response;

import com.zlatan.easy.httpclient.core.exception.HttpClientResponseBuildException;
import com.zlatan.easy.httpclient.core.response.data.HttpClientResponse;
import com.zlatan.easy.httpclient.core.response.data.HttpClientResponseImpl;
import com.zlatan.easy.httpclient.core.response.decode.ResponseBodyDecoder;
import org.apache.http.HttpResponse;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 2:32 PM
 */
public class HttpResponseBuilder<T> {

  private final ResponseBodyDecoder responseBodyDecoder;

  private final Class<? extends T> returnClass;

  public HttpResponseBuilder(ResponseBodyDecoder responseBodyDecoder) {
    this.responseBodyDecoder = responseBodyDecoder;
    this.returnClass = null;
  }

  public HttpResponseBuilder(ResponseBodyDecoder responseBodyDecoder,
                             Class<? extends T> returnClass) {
    this.responseBodyDecoder = responseBodyDecoder;
    this.returnClass = returnClass;
  }

  public HttpClientResponse<T> build(HttpResponse httpResponse) {
    try {
      T body = responseBodyDecoder.decode(httpResponse.getEntity(), returnClass);
      return new HttpClientResponseImpl<>(httpResponse, body);
    } catch (Exception e) {
      throw new HttpClientResponseBuildException(e);
    }
  }
}
