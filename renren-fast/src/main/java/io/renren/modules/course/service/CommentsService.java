package io.renren.modules.course.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.course.entity.CommentsEntity;

import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-25 15:55:18
 */
public interface CommentsService extends IService<CommentsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

