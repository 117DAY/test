package com.mnnu.examine.modules.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.mall.entity.MallProductEntity;
import com.mnnu.examine.modules.mall.vo.MallUserVO;

import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:47
 */
public interface MallProductService extends IService<MallProductEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 兑换产品
     *  @param id        id
     * @param productId 产品id
     * @param mallUserVO
     */
    void exchangeProduct(Long id, Long productId, MallUserVO mallUserVO);
}

