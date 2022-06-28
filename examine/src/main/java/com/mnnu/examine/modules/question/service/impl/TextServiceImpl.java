package com.mnnu.examine.modules.question.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.question.dao.TextDao;
import com.mnnu.examine.modules.question.entity.TextEntity;
import com.mnnu.examine.modules.question.service.TextService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("textService")
public class TextServiceImpl extends ServiceImpl<TextDao, TextEntity> implements TextService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TextEntity> page = this.page(
                new Query<TextEntity>().getPage(params),
                new QueryWrapper<TextEntity>()
        );

        return new PageUtils(page);
    }

}
