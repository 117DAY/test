package io.renren.modules.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.exam.dao.QuestionRecordDao;
import io.renren.modules.exam.entity.QuestionRecordEntity;
import io.renren.modules.exam.service.QuestionRecordService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("questionRecordService")
public class QuestionRecordServiceImpl extends ServiceImpl<QuestionRecordDao, QuestionRecordEntity> implements QuestionRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QuestionRecordEntity> page = this.page(
                new Query<QuestionRecordEntity>().getPage(params),
                new QueryWrapper<QuestionRecordEntity>()
        );

        return new PageUtils(page);
    }

}
