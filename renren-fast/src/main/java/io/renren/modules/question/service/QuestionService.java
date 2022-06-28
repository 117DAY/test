package io.renren.modules.question.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.question.entity.QuestionEntity;

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


}

