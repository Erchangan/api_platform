package com.xiaoxi.xiapi.service;

import com.xiaoxi.common.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author xiaoxi
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-09-27 09:01:27
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceinfo, boolean add);

    Object invokeInterface(String accessKey, String secretKey, long id, String userRequestParams);
}
