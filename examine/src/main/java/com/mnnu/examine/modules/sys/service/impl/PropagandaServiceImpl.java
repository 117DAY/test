package com.mnnu.examine.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.sys.dao.PropagandaDao;
import com.mnnu.examine.modules.sys.entity.PropagandaEntity;
import com.mnnu.examine.modules.sys.service.PropagandaService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("propagandaService")
public class PropagandaServiceImpl extends ServiceImpl<PropagandaDao, PropagandaEntity> implements PropagandaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PropagandaEntity> page = this.page(
                new Query<PropagandaEntity>().getPage(params),
                new QueryWrapper<PropagandaEntity>()
        );

        return new PageUtils(page);
    }

}