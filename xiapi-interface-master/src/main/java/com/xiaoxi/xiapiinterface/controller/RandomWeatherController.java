package com.xiaoxi.xiapiinterface.controller;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xiaoxi.common.model.invokecontext.request.WeatherRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class RandomWeatherController {

    @GetMapping("/getWeather")
    public String getWeather(@RequestBody WeatherRequest weatherRequest) {

        String queryString = String.format("?msg=%s&type=%s", weatherRequest.getCity(), weatherRequest.getType());
        HttpResponse httpResponse = HttpRequest.get("https://v.api.aa1.cn/api/api-tianqi-3/index.php" + queryString)
                .execute();
        return httpResponse.body();
    }
}
