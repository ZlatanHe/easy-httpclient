package com.zlatan.easy.httpclient.mapper.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 10/05/2018 5:52 PM
 */
@AllArgsConstructor
@Getter
public enum CommonBeanNames {

  GLOBAL_HTTPCLIENT("httpclient"),
  GLOBAL_HTTPCLIENT_CONFIG("easyHttpConfiguration");

  String beanName;
}
