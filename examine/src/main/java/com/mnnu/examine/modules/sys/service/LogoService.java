package com.mnnu.examine.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.sys.entity.LogoEntity;

import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
public interface LogoService extends IService<LogoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

