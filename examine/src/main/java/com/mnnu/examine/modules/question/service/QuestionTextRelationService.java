package com.mnnu.examine.modules.question.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.question.entity.QuestionTextRelationEntity;

import java.util.Map;

/**
 * 材料和(申论)试题关系表
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-01 22:30:37
 */
public interface QuestionTextRelationService extends IService<QuestionTextRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

