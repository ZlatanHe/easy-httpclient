package com.zlatan.easy.httpclient.mapper.core;

import com.zlatan.easy.httpclient.core.response.data.HttpClientResponse;

/**
 * @Title:
 * @Description:
 * @Date: Created By hewei in 09/05/2018 6:37 PM
 */
public interface HttpClientMapper<T> {

  HttpClientResponse<T> invoke(Object[] args) throws Exception;
}
