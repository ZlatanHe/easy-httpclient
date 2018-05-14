package com.zlatan.easy.httpclient.mapper.mapper.http;

import com.zlatan.easy.httpclient.core.request.encode.FormUrlEncodedRequestBodyEncoder;
import com.zlatan.easy.httpclient.core.response.data.HttpClientResponse;
import com.zlatan.easy.httpclient.core.response.decode.InputStreamBodyDecoder;
import com.zlatan.easy.httpclient.core.response.decode.JacksonResponseBodyDecoder;
import com.zlatan.easy.httpclient.mapper.annotations.*;
import com.zlatan.easy.httpclient.mapper.annotations.element.*;
import com.zlatan.easy.httpclient.mapper.constant.RequestMethod;
import com.zlatan.easy.httpclient.mapper.entity.YonyouInvoiceCreateStatusRequest;
import com.zlatan.easy.httpclient.mapper.entity.YonyouInvoiceStatusResponse;

import java.io.InputStream;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 7:28 PM
 */
@EasyHttpClient
public interface DemoClient {

  @DirectUrlHttpRequest("https://www.baidu.com")
  @ForwardProxy
  HttpClientResponse<String> testBaidu();

  @DirectUrlHttpRequest(value = "url", dynamic = true)
  @ResponseBody(decoder = InputStreamBodyDecoder.class, returnClass = InputStream.class)
  @ForwardProxy(value = "proxy-qa.s.qima-inc.com", port = 80)
  HttpClientResponse<InputStream> dynamicUrlDownload(@Url("url") String url);

  @HttpRequest(
          useSSL = true,
          method = RequestMethod.POST,
          host = "{host}",
          path = "{path}/api/invoiceApply/queryInvoiceStatus",
          encoder = FormUrlEncodedRequestBodyEncoder.class
  )
  @StaticHeaders(headers = {"Content-Type: application/x-www-form-urlencoded; charset=utf-8"})
  HttpClientResponse<String> queryYonyouInvoiceStatus(
          @Host("host") String host,
          @Path("path") String path,
          @DynamicHeader("sign") String sign,
          @Query("appid") String appId,
          @RequestEntity YonyouInvoiceCreateStatusRequest invoiceCreateStatusRequest
  );

  @HttpRequest(
          useSSL = true,
          method = RequestMethod.POST,
          host = "{host}",
          path = "{path}/api/invoiceApply/queryInvoiceStatus",
          encoder = FormUrlEncodedRequestBodyEncoder.class
  )
  @StaticHeaders(headers = {"Content-Type: application/x-www-form-urlencoded; charset=utf-8"})
  @ResponseBody(decoder = JacksonResponseBodyDecoder.class, returnClass = YonyouInvoiceStatusResponse.class)
  HttpClientResponse<YonyouInvoiceStatusResponse> queryYonyouInvoiceStatusAndGetResponse(
          @Host("host") String host,
          @Path("path") String path,
          @DynamicHeader("sign") String sign,
          @Query("appid") String appId,
          @RequestEntity YonyouInvoiceCreateStatusRequest invoiceCreateStatusRequest
  );
}
