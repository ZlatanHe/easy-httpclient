package com.zlatan.easy.httpclient.mapper;

import com.zlatan.easy.httpclient.core.util.JacksonUtils;
import com.zlatan.easy.httpclient.mapper.demo.HttpClientDemoConfig;
import com.zlatan.easy.httpclient.mapper.entity.YonyouInvoiceCreateStatusRequest;
import com.zlatan.easy.httpclient.mapper.entity.YonyouInvoiceStatusResponse;
import com.zlatan.easy.httpclient.mapper.mapper.http.DemoClient;
import com.zlatan.easy.httpclient.mapper.tool.YonyouCertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 7:31 PM
 */
@SpringBootApplication
@Import({
        HttpClientDemoConfig.class
})
@Slf4j
public class EasyDemoApplication implements CommandLineRunner {

  @Resource
  private DemoClient demoClient;

  private ConfigurableApplicationContext applicationContext;

  @Autowired
  public EasyDemoApplication(ConfigurableApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public void run(String... args) throws Exception {
    testBaidu();
    testDownloadImage();
//    testYonyouInvoiceQuery();
    applicationContext.close();
  }

  private void testBaidu() {
    demoClient.testBaidu();
  }

  private void testDownloadImage() {
    InputStream inputStream = null;
    FileOutputStream fileOutputStream = null;
    try {
      inputStream = demoClient.dynamicUrlDownload("http://img.hb.aicdn.com/4b365de2ef83cc52b7b58e94c35d263c1b91c04f25acd-Kj9yE5_fw658")
              .getBody();
      fileOutputStream = new FileOutputStream(new File("/data/tmp/宋祖儿.jpeg"));
      int buffer;
      while ((buffer = inputStream.read()) != -1) {
        fileOutputStream.write(buffer);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
        if (fileOutputStream != null) {
          fileOutputStream.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void testYonyouInvoiceQuery() throws Exception {
    YonyouInvoiceCreateStatusRequest request = new YonyouInvoiceCreateStatusRequest();
    request.setFpqqlsh("20180428153634010849");
    demoClient.queryYonyouInvoiceStatus(
            "yesfp.yonyoucloud.com",
            "invoiceclient-web",
            YonyouCertUtils.generateInvoiceSign(JacksonUtils.toJson(request), YonyouCertUtils.getPermString()),
            "commontesterCA",
            request
    );
    YonyouInvoiceStatusResponse response = demoClient.queryYonyouInvoiceStatusAndGetResponse(
            "yesfp.yonyoucloud.com",
            "invoiceclient-web",
            YonyouCertUtils.generateInvoiceSign(JacksonUtils.toJson(request), YonyouCertUtils.getPermString()),
            "commontesterCA",
            request
    ).getBody();
    log.info(JacksonUtils.toJson(response));
  }

  public static void main(String[] args) {
    SpringApplication.run(EasyDemoApplication.class, args);
  }
}
