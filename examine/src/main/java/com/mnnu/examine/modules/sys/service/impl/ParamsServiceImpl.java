package com.mnnu.examine.modules.sys.service.impl;

import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.sys.dao.ParamsDao;
import com.mnnu.examine.modules.sys.entity.ParamsEntity;
import com.mnnu.examine.modules.sys.service.ParamsService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



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