package com.xiaoxi.xiapi.service;

import com.xiaoxi.common.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author xiaoxi
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-09-27 09:01:34
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo interfaceinfo, boolean add);

    boolean invokeCount(long interfaceId, long userId);

    int getLeftNum(long interfaceId, long userId);

    boolean addInvokeCount(Long userId, Long interfaceInfoId, Integer specification);
}
