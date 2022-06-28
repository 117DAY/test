package io.renren.modules.course.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.course.entity.ViewRecordEntity;

import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
public interface ViewRecordService extends IService<ViewRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     *
     *
     * @description: 获取用户观看历史记录
     * @param page
     * @param size
     * @return: 分页对象
     * @author: Geralt
     * @time: 2021/11/26 17:47
     */
    PageUtils getViewRecordListByUserId(Map<String,Object> params,Long id);
}

