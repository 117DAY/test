package com.mnnu.examine.modules.question.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mnnu.examine.modules.question.entity.QuestionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:57
 */
@Mapper
public interface QuestionDao extends BaseMapper<QuestionEntity> {
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
     * @param limit
     * @return
     */
    List<QuestionEntity> getQuestionsByTypesRandom(@Param("types") String types, @Param("examIds") String examIds, @Param("limit") Integer limit);


    /**
     * 根据试卷类型随机获取试题
     *
     * @param type
     * @param limit
     * @return
     */
    List<QuestionEntity> getQuestionsByExamTypeRandom(@Param("type") Integer type, @Param("limit") Integer limit);
}
