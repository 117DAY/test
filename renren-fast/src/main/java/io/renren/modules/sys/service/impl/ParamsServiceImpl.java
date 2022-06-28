package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.dao.ParamsDao;
import io.renren.modules.sys.entity.ParamsEntity;
import io.renren.modules.sys.service.ParamsService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("paramsService")
public class ParamsServiceImpl extends ServiceImpl<ParamsDao, ParamsEntity> implements ParamsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ParamsEntity> page = this.page(
                new Query<ParamsEntity>().getPage(params),
                new QueryWrapper<ParamsEntity>()
        );

        return new PageUtils(page);
    }

}