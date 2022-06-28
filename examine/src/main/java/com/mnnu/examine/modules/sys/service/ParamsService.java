package com.mnnu.examine.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.sys.entity.ParamsEntity;

import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:47
 */
public interface ParamsService extends IService<ParamsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

