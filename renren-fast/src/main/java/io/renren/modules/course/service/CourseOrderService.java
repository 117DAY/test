package io.renren.modules.course.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.course.entity.CourseOrderEntity;


import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
public interface CourseOrderService extends IService<CourseOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);


    /**
     * 被用户id课程列表
     *
     * @param params 参数个数
     * @param id     id
     * @return {@link PageUtils}
     */
    PageUtils getCourseListByUserId(Map<String, Object> params, Long id);

    /**
     * 是买生活课程
     *
     * @param userId 用户id
     * @param liveId 直播id
     * @return boolean
     */
    boolean isBuyLiveCourse(String userId, String liveId);
}

