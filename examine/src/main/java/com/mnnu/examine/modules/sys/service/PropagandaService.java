package com.mnnu.examine.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.sys.entity.PropagandaEntity;

import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
public interface PropagandaService extends IService<PropagandaEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

