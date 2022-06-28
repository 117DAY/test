package com.mnnu.examine.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.sys.dao.ServiceDao;
import com.mnnu.examine.modules.sys.entity.ServiceEntity;
import com.mnnu.examine.modules.sys.service.ServiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("serviceService")
public class ServiceServiceImpl extends ServiceImpl<ServiceDao, ServiceEntity> implements ServiceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ServiceEntity> page = this.page(
                new Query<ServiceEntity>().getPage(params),
                new QueryWrapper<ServiceEntity>()
        );
        List<ServiceEntity> list = this.list();
        return new PageUtils(page);
    }

}