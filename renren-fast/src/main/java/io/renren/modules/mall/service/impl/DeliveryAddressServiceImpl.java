package io.renren.modules.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.mall.dao.DeliveryAddressDao;
import io.renren.modules.mall.entity.DeliveryAddressEntity;
import io.renren.modules.mall.service.DeliveryAddressService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("deliveryAddressService")
public class DeliveryAddressServiceImpl extends ServiceImpl<DeliveryAddressDao, DeliveryAddressEntity> implements DeliveryAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DeliveryAddressEntity> page = this.page(
                new Query<DeliveryAddressEntity>().getPage(params),
                new QueryWrapper<DeliveryAddressEntity>()
        );

        return new PageUtils(page);
    }

}