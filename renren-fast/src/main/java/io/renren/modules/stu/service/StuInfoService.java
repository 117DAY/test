package io.renren.modules.stu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.stu.entity.StuInfoEntity;


import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
public interface StuInfoService extends IService<StuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 更新积分
     *
     * @param stuId 学生id
     * @param addPoint
     */
    void updatePoint(Long stuId, BigDecimal addPoint);


    /**
     * 减去因退款而减少的积分
     *
     * @param courseId   进程id
     * @param courseType 课程类型
     * @param userId     用户id
     * @param payPrice   支付的价格
     */
    void subtractPoint(Integer courseId, Integer courseType, Long userId, BigDecimal payPrice);
}

