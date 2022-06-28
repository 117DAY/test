package com.mnnu.examine.modules.course.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.course.entity.CourseOrderEntity;
import com.mnnu.examine.modules.course.service.CourseOrderService;
import com.mnnu.examine.modules.sys.entity.RefundEntity;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
@RestController
@RequestMapping("course/courseorder")
public class CourseOrderController {
    @Autowired
    private CourseOrderService courseOrderService;

    @Resource
    RefundService refundService;

    @Resource(name = "stringRedisTemplate")
    StringRedisTemplate redisTemplate;

    /**
     * @param limit
     * @param page
     * @param type(0-普通课程,1-直播课程,2-订单信息)
     * @description: 根据用户ID获取用户已购买课程列表 (或订单信息)
     * @return: 课程分页
     * @author: Geralt
     * @time: 2021/11/26 17:44
     */
    @RequestMapping("/userCourseList")
    public R getCourseListByUserId(@RequestParam Map<String, Object> params) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        PageUtils page = courseOrderService.getCourseListByUserId(params, id);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = courseOrderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CourseOrderEntity courseOrder = courseOrderService.getById(id);

        return R.ok().put("courseOrder", courseOrder);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CourseOrderEntity courseOrder) {
        courseOrderService.save(courseOrder);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CourseOrderEntity courseOrder) {
        courseOrderService.updateById(courseOrder);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable("id") String orderId) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        CourseOrderEntity courseOrderEntity = courseOrderService.getOne(new QueryWrapper<CourseOrderEntity>().eq("order_id", orderId));
        if (courseOrderEntity == null) {
            return R.error();
        }
        if (courseOrderEntity.getStatus() == 3 && courseOrderEntity.getUserId().equals(id)) {
            courseOrderService.removeById(courseOrderEntity.getId());
            return R.ok();
        } else {
            return R.error();
        }
    }


    /**
     * 退款
     *
     * @param orderId 订单id
     * @param reason  原因
     * @return {@link R}
     */
    @RequestMapping("/refund/{id}")
    public R refund(@PathVariable("id") String orderId, @RequestBody Map<String,String> obj) {
        String reason=obj.get("reason");
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        CourseOrderEntity courseOrderEntity = courseOrderService.getOne(new QueryWrapper<CourseOrderEntity>().eq("order_id", orderId));
        if (courseOrderEntity == null) {
            return R.error("当前订单不存在");
        }
        if (courseOrderEntity.getStatus().equals(GwyConstant.OrderStatus.PAID.getCode()) && courseOrderEntity.getUserId().equals(id)) {
            courseOrderEntity.setStatus(GwyConstant.OrderStatus.APPLY_REFUND.getCode());
            courseOrderService.updateById(courseOrderEntity);
            // 维护refund表
            refundService.saveRefundInfo(courseOrderEntity);
            redisTemplate.opsForValue().set(orderId, reason);
            return R.ok();
        } else {
            return R.error("当前订单状态不正确，请确认订单号");
        }
    }

    @RequestMapping("/cancelRefund/{id}")
    public R cancelRefund(@PathVariable("id") String orderId) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        CourseOrderEntity courseOrderEntity = courseOrderService.getOne(new QueryWrapper<CourseOrderEntity>().eq("order_id", orderId));
        if (courseOrderEntity == null) {
            return R.error();
        }
        if (courseOrderEntity.getStatus().equals(GwyConstant.OrderStatus.APPLY_REFUND.getCode()) && courseOrderEntity.getUserId().equals(id)) {
            courseOrderEntity.setStatus(GwyConstant.OrderStatus.PAID.getCode());
            courseOrderService.updateById(courseOrderEntity);
            // 删除退款记录
            refundService.remove(new QueryWrapper<RefundEntity>().eq("order_id", courseOrderEntity.getOrderId()));
            return R.ok();
        } else {
            return R.error();
        }
    }


}
