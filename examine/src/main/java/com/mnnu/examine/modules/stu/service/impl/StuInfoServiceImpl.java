package com.mnnu.examine.modules.stu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.stu.dao.StuInfoDao;
import com.mnnu.examine.modules.stu.entity.StuInfoEntity;
import com.mnnu.examine.modules.stu.service.StuInfoService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service("stuInfoService")
public class StuInfoServiceImpl extends ServiceImpl<StuInfoDao, StuInfoEntity> implements StuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StuInfoEntity> page = this.page(
                new Query<StuInfoEntity>().getPage(params),
                new QueryWrapper<StuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void updatePoint(Long stuId, BigDecimal addPoint) {
        getBaseMapper().updatePoint(stuId, addPoint);
    }

}