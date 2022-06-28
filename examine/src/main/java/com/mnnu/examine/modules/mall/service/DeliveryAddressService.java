package com.mnnu.examine.modules.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.mall.entity.DeliveryAddressEntity;

import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:48
 */
public interface DeliveryAddressService extends IService<DeliveryAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 改变默认的地址
     *
     * @param userId    用户id
     * @param addressId 地址标识
     */
    void changeDefault(Long userId, Long addressId);
}

