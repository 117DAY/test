package io.renren.modules.question.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.exam.entity.ExamEntity;
import io.renren.modules.question.entity.QuestionEntity;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 自动生成
 * @email generat
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
    List<QuestionEntity> getQuestionsByTypesRandom(String types, String examIds, Integer limit);

}
