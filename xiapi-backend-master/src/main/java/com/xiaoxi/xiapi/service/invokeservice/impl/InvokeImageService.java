package com.xiaoxi.xiapi.service.invokeservice.impl;

import com.xiaoxi.common.model.invokecontext.response.ApiResponse;
import com.xiaoxi.xiapi.service.invokeservice.InvokeService;
import com.xiaoxi.xiapiclientsdk.client.ApiClient;

public class InvokeImageService implements InvokeService {
    @Override
    public ApiResponse invokeInterface(String accessKey, String secretKey, String userRequestParams) {
        ApiClient apiClient = new ApiClient(accessKey, secretKey);
        return apiClient.getRandomImage();
    }
}
