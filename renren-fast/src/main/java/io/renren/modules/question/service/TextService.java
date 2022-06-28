package io.renren.modules.question.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.question.entity.TextEntity;

import java.util.Map;

/**
 * (申论)试题材料表
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-01 22:30:37
 */
public interface TextService extends IService<TextEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

