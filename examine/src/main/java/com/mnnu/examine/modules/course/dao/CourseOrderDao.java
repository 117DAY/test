package com.mnnu.examine.modules.course.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mnnu.examine.modules.course.entity.CourseOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
@Mapper
public interface CourseOrderDao extends BaseMapper<CourseOrderEntity> {

    /**
     * 查询已支付的或正在退款中的订单是否存在
     *
     * @param userId     用户id
     * @param courseId   进程id
     * @param courseType 课程类型
     * @return int
     */
    List<CourseOrderEntity> queryOrderIfExist(@Param("userId") Long userId, @Param("courseId") Integer courseId, @Param("courseType") Integer courseType);

    /**
     * 更新显示状态
     *
     * @param id id
     */
    void updateShowStatus(@Param("id") Long id);
}
