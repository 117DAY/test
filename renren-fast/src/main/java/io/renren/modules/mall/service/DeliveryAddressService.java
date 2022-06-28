package io.renren.modules.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.mall.entity.DeliveryAddressEntity;


import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:48
 */
public interface DeliveryAddressService extends IService<DeliveryAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

