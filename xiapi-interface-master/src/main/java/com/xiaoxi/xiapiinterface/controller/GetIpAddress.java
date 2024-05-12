package com.xiaoxi.xiapiinterface.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xiaoxi.common.model.invokecontext.request.IpAddressRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ip")
public class GetIpAddress {
    @GetMapping("/getAddress")
    public String getAddress(@RequestBody IpAddressRequest ipAddressRequest) {

        String queryString = String.format("?ip=%s", ipAddressRequest.getIp());
        HttpResponse httpResponse = HttpRequest.get("http://api.rlbot.cn/api/dm-iplocation" + queryString)
                .execute();
        return httpResponse.body();
    }
}
