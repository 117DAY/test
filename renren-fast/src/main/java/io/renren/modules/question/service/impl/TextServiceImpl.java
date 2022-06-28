package io.renren.modules.question.service.impl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.question.dao.TextDao;
import io.renren.modules.question.entity.TextEntity;
import io.renren.modules.question.service.TextService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



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