package com.zlatan.easy.httpclient.mapper.entity;

import com.zlatan.easy.httpclient.mapper.annotations.element.RequestBody;
import lombok.Data;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 11/05/2018 2:59 PM
 */
@Data
public class YonyouInvoiceCreateStatusRequest {

  /**
   * 发票请求流水号
   */
  @RequestBody("fpqqlsh")
  private String fpqqlsh;
}