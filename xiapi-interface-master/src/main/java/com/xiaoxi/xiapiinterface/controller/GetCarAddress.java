package com.xiaoxi.xiapiinterface.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xiaoxi.common.model.invokecontext.request.CarAddressRequest;
import com.xiaoxi.common.model.invokecontext.request.IpAddressRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/car")
public class GetCarAddress {
    @GetMapping("/getAddress")
    public String getAddress(@RequestBody CarAddressRequest carAddressRequest) {

        String queryString = String.format("?word=%s", carAddressRequest.getWord());
        HttpResponse httpResponse = HttpRequest.get("http://api.rlbot.cn/api/dm-chepai" + queryString)
                .execute();
        return httpResponse.body();
    }
}
