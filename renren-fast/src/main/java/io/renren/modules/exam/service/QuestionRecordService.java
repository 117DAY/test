package io.renren.modules.exam.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.exam.entity.QuestionRecordEntity;

import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:57
 */
public interface QuestionRecordService extends IService<QuestionRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

