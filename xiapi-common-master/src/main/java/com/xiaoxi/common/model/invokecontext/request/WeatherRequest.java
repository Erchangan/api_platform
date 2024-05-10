package com.xiaoxi.common.model.invokecontext.request;

import lombok.Data;

@Data
public class WeatherRequest {
    private String city;
    private String type;
}
