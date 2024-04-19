package com.xiaoxi.xiapi.service;

import com.xiaoxi.common.model.entity.InterfaceInfoProduct;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author xiaoxi
* @description 针对表【interface_info_product(接口商品)】的数据库操作Service
* @createDate 2023-10-17 14:58:37
*/
public interface InterfaceInfoProductService extends IService<InterfaceInfoProduct> {

    void validInterfaceInfoProduct(InterfaceInfoProduct interfaceInfoProduct, boolean add);

    InterfaceInfoProduct orderProduct(Long interfaceInfoId, Long interfaceInfoProductId);
}
