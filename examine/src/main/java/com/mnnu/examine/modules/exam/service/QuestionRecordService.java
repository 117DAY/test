package com.mnnu.examine.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.exam.entity.QuestionRecordEntity;

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

