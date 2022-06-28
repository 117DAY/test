package com.mnnu.examine.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.sys.dao.CatchphraseDao;
import com.mnnu.examine.modules.sys.entity.CatchphraseEntity;
import com.mnnu.examine.modules.sys.service.CatchphraseService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("catchphraseService")
public class CatchphraseServiceImpl extends ServiceImpl<CatchphraseDao, CatchphraseEntity> implements CatchphraseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CatchphraseEntity> page = this.page(
                new Query<CatchphraseEntity>().getPage(params),
                new QueryWrapper<CatchphraseEntity>()
        );

        return new PageUtils(page);
    }

}