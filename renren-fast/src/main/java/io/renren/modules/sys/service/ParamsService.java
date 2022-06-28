package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.ParamsEntity;


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

