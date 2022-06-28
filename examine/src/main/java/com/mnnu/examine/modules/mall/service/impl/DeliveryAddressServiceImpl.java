package com.mnnu.examine.modules.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.mall.dao.DeliveryAddressDao;
import com.mnnu.examine.modules.mall.entity.DeliveryAddressEntity;
import com.mnnu.examine.modules.mall.service.DeliveryAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("deliveryAddressService")
public class DeliveryAddressServiceImpl extends ServiceImpl<DeliveryAddressDao, DeliveryAddressEntity> implements DeliveryAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DeliveryAddressEntity> page = this.page(
                new Query<DeliveryAddressEntity>().getPage(params),
                new QueryWrapper<DeliveryAddressEntity>()
        );
        List<DeliveryAddressEntity> records = page.getRecords();
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeDefault(Long userId, Long addressId) {
        DeliveryAddressEntity addressEntity = this.getOne(new QueryWrapper<DeliveryAddressEntity>().eq("id", addressId).and(w -> w.eq("user_id", userId)));
        if (addressEntity == null) {
            throw new RuntimeException("地址错误，请检查");
        }

        if (addressEntity.getIsDefault() == 1) {
            return;
        }
        // 更新地址
        // 清除默认地址
        getBaseMapper().clearDefault(userId);

        // 设置默认地址
        addressEntity.setIsDefault(1);
        this.updateById(addressEntity);
    }

}
