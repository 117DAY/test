package io.renren.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.GwyCatchphraseDao;
import io.renren.modules.sys.entity.GwyCatchphraseEntity;
import io.renren.modules.sys.service.GwyCatchphraseService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("catchphraseService")
public class GwyCatchphraseServiceImpl extends ServiceImpl<GwyCatchphraseDao, GwyCatchphraseEntity> implements GwyCatchphraseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<GwyCatchphraseEntity> page = this.page(
                new Query<GwyCatchphraseEntity>().getPage(params),
                new QueryWrapper<GwyCatchphraseEntity>()
        );

        return new PageUtils(page);
    }

}
