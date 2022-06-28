package com.mnnu.examine.modules.question.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.question.dao.QuestionDao;
import com.mnnu.examine.modules.question.entity.QuestionEntity;
import com.mnnu.examine.modules.question.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("questionService")
public class QuestionServiceImpl extends ServiceImpl<QuestionDao, QuestionEntity> implements QuestionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QuestionEntity> page = this.page(
                new Query<QuestionEntity>().getPage(params),
                new QueryWrapper<QuestionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<QuestionEntity> getQuestionWithTexts(Integer id) {
        return this.getBaseMapper().getQuestionWithTexts(id);
    }

    @Override
    public List<QuestionEntity> getQuestionsByTypesRandom(String types, String examIds, Integer limit) {
        return this.getBaseMapper().getQuestionsByTypesRandom(types, examIds, limit);
    }

    @Override
    public List<QuestionEntity> getQuestionsByExamTypeRandom(Integer type, Integer limit) {
        return this.getBaseMapper().getQuestionsByExamTypeRandom(type, limit);
    }


}
