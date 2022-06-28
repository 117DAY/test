package com.mnnu.examine.modules.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mnnu.examine.modules.mall.entity.DeliveryAddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:48
 */
@Mapper
public interface DeliveryAddressDao extends BaseMapper<DeliveryAddressEntity> {

    void clearDefault(@Param("userId") Long userId);
}
