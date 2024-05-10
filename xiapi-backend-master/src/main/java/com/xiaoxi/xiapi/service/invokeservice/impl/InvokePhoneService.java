package com.xiaoxi.xiapi.service.invokeservice.impl;

import com.xiaoxi.common.model.invokecontext.paramtype.KeyValueParam;
import com.xiaoxi.common.model.invokecontext.response.ApiResponse;
import com.xiaoxi.xiapi.service.invokeservice.InvokeService;
import com.xiaoxi.xiapiclientsdk.client.ApiClient;
import com.xiaoxi.xiapiclientsdk.utils.AnalyzeRequestParamsUtils;

import java.util.List;

public class InvokePhoneService implements InvokeService {
    @Override
    public ApiResponse invokeInterface(String accessKey, String secretKey, String userRequestParams) {
        ApiClient apiClient = new ApiClient(accessKey, secretKey);
        List<KeyValueParam> paramList = AnalyzeRequestParamsUtils.getParamList(userRequestParams);
        String paramValue1 = paramList.get(0).getParamValue();
        return apiClient.getPhoneAddress(paramValue1);
    }
}
