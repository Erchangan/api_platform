package com.xiaoxi.xiapiinterface.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xiaoxi.common.model.invokecontext.request.PhoneRequest;
import com.xiaoxi.common.model.invokecontext.request.WeatherRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/phone")
public class PhoneAddressController {
    @GetMapping("/getAddress")
    public String getAddress(@RequestBody PhoneRequest phoneRequest) {

        String queryString = String.format("?phone=%s", phoneRequest.getPhone());
        HttpResponse httpResponse = HttpRequest.get("https://api.52vmy.cn/api/query/phone" + queryString)
                .execute();
        return httpResponse.body();
    }
}
