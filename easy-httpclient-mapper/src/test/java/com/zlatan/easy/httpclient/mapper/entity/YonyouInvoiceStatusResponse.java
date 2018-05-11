package com.zlatan.easy.httpclient.mapper.entity;

import lombok.Data;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 11/05/2018 5:56 PM
 */
@Data
public class YonyouInvoiceStatusResponse {

  /**
   * 发票请求流水号
   */
  private String fpqqlsh;

  /**
   * 状态码
   * 
   */
  private String code;

  /**
   * 信息说明
   */
  private String msg;

  /**
   * 开票状态码
   * 
   */
  private String statuscode;

  /**
   * 开票状态
   */
  private String status;

  /**
   * 开票失败明细
   */
  private String errmsg;

  /**
   * 开票内容，需要2次解析
   * 
   */
  private String data;
}
