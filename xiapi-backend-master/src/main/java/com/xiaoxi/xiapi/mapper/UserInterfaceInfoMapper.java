package com.xiaoxi.xiapi.mapper;

import com.xiaoxi.common.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author xiaoxi
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2023-09-27 09:01:34
* @Entity com.xiaoxi.xiapi.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    Long getInvokeMoreInterfaceByTotalNum(@Param("userId") Long userId);
}




