package com.zlatan.easy.httpclient.mapper.scan;

import com.zlatan.easy.httpclient.mapper.annotations.HttpClient;
import com.zlatan.easy.httpclient.mapper.constant.CommonBeanNames;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.util.Set;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 5:46 PM
 */
public class HttpClientMapperDefinitionScanner extends ClassPathBeanDefinitionScanner {

  HttpClientMapperDefinitionScanner(BeanDefinitionRegistry registry) {
    super(registry);
    addIncludeFilter(new AnnotationTypeFilter(HttpClient.class));
  }

  @Override
  protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
    return metadataReader.getAnnotationMetadata().hasAnnotation(HttpClient.class.getName());
  }

  @Override
  protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
    return beanDefinition.getMetadata().isIndependent() && beanDefinition.getMetadata().isInterface();
  }

  @Override
  protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
    Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
    for (BeanDefinitionHolder definitionHolder : beanDefinitionHolders) {
      GenericBeanDefinition definition = (GenericBeanDefinition) definitionHolder.getBeanDefinition();
      definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
      definition.getPropertyValues().add(
              "httpClient",
              new RuntimeBeanReference(CommonBeanNames.GLOBAL_HTTPCLIENT.getBeanName())
      );
      definition.getPropertyValues().add(
              "easyHttpConfiguration",
              new RuntimeBeanReference(CommonBeanNames.GLOBAL_HTTPCLIENT_CONFIG.getBeanName())
      );
      definition.setBeanClass(HttpClientMapperFactoryBean.class);
    }
    return beanDefinitionHolders;
  }
}
