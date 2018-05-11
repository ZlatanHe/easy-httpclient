package com.zlatan.easy.httpclient.mapper.demo;

import com.zlatan.easy.httpclient.mapper.configuration.EasyHttpConfiguration;
import com.zlatan.easy.httpclient.mapper.core.HttpClientFactoryBean;
import com.zlatan.easy.httpclient.mapper.scan.HttpClientMapperBeanRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 5:54 PM
 */
@Configuration
@Import({
        PropertyPlaceholderAutoConfiguration.class
})
@EnableConfigurationProperties
public class HttpClientDemoConfig {

  @Bean("easyHttpConfiguration")
  EasyHttpConfiguration easyHttpConfiguration() {
    return new EasyHttpConfiguration();
  }

  @Bean("httpclient")
  public HttpClientFactoryBean httpClient() {
    return new HttpClientFactoryBean(easyHttpConfiguration());
  }

  @Bean
  @Autowired
  public static HttpClientMapperBeanRegistry httpClientMapperBeanRegistry(ConfigurableEnvironment environment)  {
    String strBasePackages = environment.resolvePlaceholders("${easy.httpClient.basePackages:}");
    List<String> basePackages = new ArrayList<>();
    if (strBasePackages != null) {
      basePackages = Arrays.asList(strBasePackages.split(";"));
    }
    String basePackage = environment.resolvePlaceholders("${easy.httpClient.basePackage:}");

    HttpClientMapperBeanRegistry registry = new HttpClientMapperBeanRegistry();
    registry.setBasePackage(basePackage);
    registry.setBasePackages(basePackages);
    return registry;
  }
}
