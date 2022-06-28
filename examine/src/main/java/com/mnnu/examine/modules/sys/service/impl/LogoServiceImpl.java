package com.mnnu.examine.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.sys.dao.LogoDao;
import com.mnnu.examine.modules.sys.entity.LogoEntity;
import com.mnnu.examine.modules.sys.service.LogoService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("logoService")
public class LogoServiceImpl extends ServiceImpl<LogoDao, LogoEntity> implements LogoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LogoEntity> page = this.page(
                new Query<LogoEntity>().getPage(params),
                new QueryWrapper<LogoEntity>()
        );

        return new PageUtils(page);
    }

}