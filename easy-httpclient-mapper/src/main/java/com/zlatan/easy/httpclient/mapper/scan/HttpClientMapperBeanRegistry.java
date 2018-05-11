package com.zlatan.easy.httpclient.mapper.scan;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 5:50 PM
 */
public class HttpClientMapperBeanRegistry implements BeanDefinitionRegistryPostProcessor {

  private String basePackage;

  private List<String> basePackages;

  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }

  public void setBasePackages(List<String> basePackages) {
    this.basePackages = basePackages;
  }

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    HttpClientMapperDefinitionScanner scanner = new HttpClientMapperDefinitionScanner(registry);

    List<String> basePackages = new ArrayList<>();
    if (this.basePackages != null) {
      basePackages.addAll(this.basePackages);
    }
    if (this.basePackage != null) {
      String[] basePackageArray = StringUtils.tokenizeToStringArray(
              this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
      if (basePackageArray != null) {
        for (String basePackage : basePackageArray) {
          basePackages.add(basePackage);
        }
      }
    }

    if (basePackages.size() > 0) {
      scanner.scan(basePackages.toArray(new String[basePackages.size()]));
    }
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
  }
}
