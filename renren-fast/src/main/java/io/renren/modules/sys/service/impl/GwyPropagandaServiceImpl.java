package io.renren.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.dao.GwyPropagandaDao;
import io.renren.modules.sys.entity.GwyPropagandaEntity;
import io.renren.modules.sys.service.GwyPropagandaService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("propagandaService")
public class GwyPropagandaServiceImpl extends ServiceImpl<GwyPropagandaDao, GwyPropagandaEntity> implements GwyPropagandaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<GwyPropagandaEntity> page = this.page(
                new Query<GwyPropagandaEntity>().getPage(params),
                new QueryWrapper<GwyPropagandaEntity>()
        );

        return new PageUtils(page);
    }

}