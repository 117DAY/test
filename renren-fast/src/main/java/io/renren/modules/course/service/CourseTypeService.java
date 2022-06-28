package io.renren.modules.course.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.course.entity.CourseTypeEntity;

import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-26 10:24:40
 */
public interface CourseTypeService extends IService<CourseTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

