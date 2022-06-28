package io.renren.modules.question.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.question.dao.QuestionTextRelationDao;
import io.renren.modules.question.entity.QuestionTextRelationEntity;
import io.renren.modules.question.service.QuestionTextRelationService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("questionTextRelationService")
public class QuestionTextRelationServiceImpl extends ServiceImpl<QuestionTextRelationDao, QuestionTextRelationEntity> implements QuestionTextRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QuestionTextRelationEntity> page = this.page(
                new Query<QuestionTextRelationEntity>().getPage(params),
                new QueryWrapper<QuestionTextRelationEntity>()
        );

        return new PageUtils(page);
    }

}