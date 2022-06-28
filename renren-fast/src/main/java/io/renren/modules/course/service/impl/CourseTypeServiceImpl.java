package io.renren.modules.course.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.course.dao.CourseTypeDao;
import io.renren.modules.course.entity.CourseTypeEntity;
import io.renren.modules.course.service.CourseTypeService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("courseTypeService")
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeDao, CourseTypeEntity> implements CourseTypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CourseTypeEntity> page = this.page(
                new Query<CourseTypeEntity>().getPage(params),
                new QueryWrapper<CourseTypeEntity>()
        );

        return new PageUtils(page);
    }

}