package com.mnnu.examine.modules.question.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.question.entity.QuestionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:57
 */
public interface QuestionService extends IService<QuestionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据试题查询材料列表
     *
     * @param id
     * @return
     */
    List<QuestionEntity> getQuestionWithTexts(Integer id);


    /**
     * 根据多种分类id随机获取试题
     *
     * @param types
     * @param examIds
     * @return
     */
    List<QuestionEntity> getQuestionsByTypesRandom(String types, String examIds, Integer limit);

    /**
     * 根据试卷类型随机获取试题
     *
     * @param type
     * @param limit
     * @return
     */
    List<QuestionEntity> getQuestionsByExamTypeRandom(@Param("type") Integer type, @Param("limit") Integer limit);
}

