package io.renren.modules.course.controller;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.course.entity.CourseOrderEntity;
import io.renren.modules.course.service.CourseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
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


    /**
     * @param limit
     * @param page
     * @param type(0-普通课程,1-直播课程,3-订单信息)
     * @description: 根据用户ID获取用户已购买课程列表 (或订单信息)
     * @return: 课课程分页
     * @author: Geralt
     * @time: 2021/11/26 17:44
     */
    @RequestMapping("/userCourseList/{id}")
    public R getCourseListByUserId(@RequestParam Map<String, Object> params, @PathVariable Long id) {

        PageUtils page = courseOrderService.getCourseListByUserId(params, id);
        return R.ok().put("page", page);
    }

    /**
     * status 订单状态0新建未支付、1已支付、2申请退款、3已退款
     * orderId 订单号
     * payMode 微信或支付宝
     * courseId 课程id
     * courseType 课程类型
     * <p>
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
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        courseOrderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @Resource(name = "stringRedisTemplate")
    StringRedisTemplate redisTemplate;

    /**
     * 得到退款的原因
     *
     *
     * @return {@link R}
     */
    @RequestMapping("/refund/reason")
    public R getRefundReason(@RequestBody Map<String,String> map) {
        String reason = redisTemplate.opsForValue().get(map.get("orderId"));
        return R.ok().put("data", reason);
    }

}
