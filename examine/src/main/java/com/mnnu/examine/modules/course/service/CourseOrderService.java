package com.mnnu.examine.modules.course.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.course.entity.CourseOrderEntity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
public interface CourseOrderService extends IService<CourseOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);


    /**
     * 用户id课程列表
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


    /**
     * 创建订单
     *
     * @param userId     用户id
     * @param courseId   课程id
     * @param courseType 课程类型
     * @param payPrice   支付的价格
     * @param order      订单
     * @param payMode    支付方式
     */
    void createOrder(Long userId, Long courseId, Integer courseType, BigDecimal payPrice, String order, String payMode);

    /**
     * 更新订单状态
     *
     * @param order     订单
     * @param serialNum 串行num
     * @param status    状态
     */
    void updateOrderStatus(String order, String serialNum, Integer status);

    /**
     * 订单是否存在
     *
     * @param userId     用户id
     * @param courseId   进程id
     * @param courseType 课程类型
     * @return {@link Boolean}
     */
    String orderIfExist(Long userId, Integer courseId, Integer courseType);

}

