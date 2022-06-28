package io.renren.modules.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.mall.entity.MallProductEntity;


import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:47
 */
public interface MallProductService extends IService<MallProductEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

