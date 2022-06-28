package io.renren.modules.question.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.question.dao.QuestionDao;
import io.renren.modules.question.entity.QuestionEntity;
import io.renren.modules.question.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("questionService")
public class QuestionServiceImpl extends ServiceImpl<QuestionDao, QuestionEntity> implements QuestionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<QuestionEntity> queryWrapper = new QueryWrapper<>();
        Integer typeId = (Integer) params.get("typeId");
        if (typeId == null) {
            queryWrapper.eq("type_id", typeId);
        }
        IPage<QuestionEntity> page = this.page(
                new Query<QuestionEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public List<QuestionEntity> getQuestionWithTexts(Integer id) {
        return this.getBaseMapper().getQuestionWithTexts(id);
    }

    @Override
    public List<QuestionEntity> getQuestionsByTypesRandom(String types,String examIds, Integer limit) {
        return this.getBaseMapper().getQuestionsByTypesRandom(types, examIds, limit);
    }


}
