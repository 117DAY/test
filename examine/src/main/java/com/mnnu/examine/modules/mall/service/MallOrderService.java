package com.mnnu.examine.modules.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.mall.entity.MallOrderEntity;


import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 22:38:40
 */
public interface MallOrderService extends IService<MallOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils getCourseListByUserId(Map<String, Object> params, Long id);
}

