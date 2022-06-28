package com.mnnu.examine.modules.course.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.course.entity.CourseTypeEntity;

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

