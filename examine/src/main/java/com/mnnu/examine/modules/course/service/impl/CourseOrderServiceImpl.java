package com.mnnu.examine.modules.course.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.course.dao.CourseOrderDao;
import com.mnnu.examine.modules.course.entity.CourseEntity;
import com.mnnu.examine.modules.course.entity.CourseOrderEntity;
import com.mnnu.examine.modules.course.service.CourseOrderService;
import com.mnnu.examine.modules.course.service.CourseService;
import com.mnnu.examine.modules.course.vo.OrderCourseLiveVO;
import com.mnnu.examine.modules.live.entity.LiveEntity;
import com.mnnu.examine.modules.live.service.LiveService;
import com.mnnu.examine.modules.stu.service.StuInfoService;
import com.mnnu.examine.modules.teacher.service.TeacherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单
 *
 * @author qiaoh
 * @date 2021/12/05
 */
@Service("courseOrderService")
public class CourseOrderServiceImpl extends ServiceImpl<CourseOrderDao, CourseOrderEntity> implements CourseOrderService {

    @Resource
    TeacherInfoService teacherInfoService;

    @Resource
    StuInfoService stuInfoService;

    @Autowired
    CourseService courseService;
    @Autowired
    LiveService liveService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CourseOrderEntity> page = this.page(
                new Query<CourseOrderEntity>().getPage(params),
                new QueryWrapper<CourseOrderEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * @param params limit
     * @param params page
     * @param params type(0-普通课程,1-直播课程,2-订单信息)
     * @description: 根据用户ID获取用户已购买课程列表 (或订单信息)
     * @return: 课课程分页
     * @author: Geralt
     * @time: 2021/11/26 17:44
     */
    @Override
    public PageUtils getCourseListByUserId(Map<String, Object> params, Long userId) {
        int type = Integer.parseInt((String) params.get("type"));
        QueryWrapper<CourseOrderEntity> wrapper = (type == 0 || type == 1) ? new QueryWrapper<CourseOrderEntity>().
                eq("user_id", userId).eq("course_type", type).eq("status", GwyConstant.OrderStatus.PAID.getCode()) : new QueryWrapper<CourseOrderEntity>().
                eq("user_id", userId).orderByDesc("order_id");
        IPage<CourseOrderEntity> page = this.page(
                new Query<CourseOrderEntity>().getPage(params), wrapper
        );
        PageUtils orderPage = new PageUtils(page);
        List<CourseOrderEntity> orderList = (List<CourseOrderEntity>) orderPage.getList();

        if (type == 0 || type == 1) {
            List<Long> courseIdList = orderList.stream().map(CourseOrderEntity::getCourseId).collect(Collectors.toList());
            if (courseIdList.size() == 0) {
                orderPage.setList(null);
                return orderPage;
            }
            if (type == 0) {
                List<CourseEntity> courseEntityList = courseService.getBaseMapper().selectBatchIds(courseIdList);
                orderPage.setList(courseEntityList);
            } else if (type == 1) {
                List<LiveEntity> liveEntityList = liveService.getBaseMapper().selectBatchIds(courseIdList);
                orderPage.setList(liveEntityList);
            }

        } else if (type == 2) {
            List<OrderCourseLiveVO> orderCourseLiveVOS = new ArrayList<>();
            for (CourseOrderEntity courseOrderEntity : orderList) {
                OrderCourseLiveVO tempOrderCourseLiveVO = new OrderCourseLiveVO();
                if (courseOrderEntity.getCourseType() == 1) {
                    LiveEntity liveTemp = liveService.getBaseMapper().selectById(courseOrderEntity.getCourseId());
                    if (liveTemp != null) {
                        tempOrderCourseLiveVO.setCourseTitle(liveTemp.getCourseTitle());
                    }
                } else {
                    CourseEntity courseTemp = courseService.getBaseMapper().selectById(courseOrderEntity.getCourseId());
                    if (courseTemp != null) {
                        tempOrderCourseLiveVO.setCourseTitle(courseTemp.getCourseTitle());
                    }
                }
                tempOrderCourseLiveVO.setOrderId(courseOrderEntity.getOrderId());
                tempOrderCourseLiveVO.setCourseType(courseOrderEntity.getCourseType());
                tempOrderCourseLiveVO.setStatus(courseOrderEntity.getStatus());
                tempOrderCourseLiveVO.setCreateTime(courseOrderEntity.getCreateTime());
                tempOrderCourseLiveVO.setPayMode(courseOrderEntity.getPayMode());
                tempOrderCourseLiveVO.setPayPrice(courseOrderEntity.getPayPrice());
                orderCourseLiveVOS.add(tempOrderCourseLiveVO);
            }
            orderPage.setList(orderCourseLiveVOS);

        }
        return orderPage;
    }

    /**
     * @param userId
     * @param liveId
     * @description: 查询用户是否购买直播课程
     * @return: bool
     * @author: Geralt
     * @time: 2021/11/26 16:45
     */
    @Override
    public boolean isBuyLiveCourse(String userId, String liveId) {
        QueryWrapper<CourseOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id", userId)
                .eq("course_type", 1)
                .eq("course_id", liveId)
                .eq("status", 1);//确认已经购买了
        if (getBaseMapper().selectCount(queryWrapper) >= 1) return true;
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Long userId, Long courseId, Integer courseType, BigDecimal payPrice, String order, String payMode) {
        CourseOrderEntity orderEntity = new CourseOrderEntity();
        orderEntity.setUserId(userId);
        orderEntity.setCourseId(courseId);
        orderEntity.setOrderId(order);
        orderEntity.setCourseType(courseType);
        orderEntity.setPayMode(payMode);
        orderEntity.setPayPrice(payPrice);
        orderEntity.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        orderEntity.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        orderEntity.setStatus(GwyConstant.OrderStatus.NEW.getCode());
        this.save(orderEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatus(String order, String serialNum, Integer status) {
        // 更新用户订单状态以及填充支付宝流水号
        CourseOrderEntity orderEntity = this.getOne(new QueryWrapper<CourseOrderEntity>().eq("order_id", order));
        if (orderEntity == null) {
            throw new RuntimeException("支付流程出现错误，当前的订单号数据不存在数据库中");
        }
        orderEntity.setSerialNum(serialNum);
        orderEntity.setStatus(GwyConstant.OrderStatus.PAID.getCode());
        orderEntity.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.updateById(orderEntity);
        // 更新抽成
        Long courseId = orderEntity.getCourseId();
        Integer courseType = orderEntity.getCourseType();
        updatePointAndCommistion(courseId, courseType, orderEntity.getUserId(), orderEntity.getPayPrice());
    }

    private void updatePointAndCommistion(Long courseId, Integer courseType, Long stuId, BigDecimal payPrice) {
        BigDecimal pointPercent = null;
        BigDecimal commisionPercent = null;
        Integer teacherId = null;

        // 更新学生的积分和教师的抽成
        if (courseType.equals(GwyConstant.CourseType.RECORDED.getType())) {

            CourseEntity courseEntity = courseService.getById(courseId);
            pointPercent = courseEntity.getPointPercent();
            commisionPercent = courseEntity.getCommisionPercent();
            teacherId = courseEntity.getTeacherId();
        } else if (courseType.equals(GwyConstant.CourseType.LIVE.getType())) {
            LiveEntity liveEntity = liveService.getById(courseId);
            pointPercent = liveEntity.getPointPercent();
            commisionPercent = liveEntity.getCommisionPercent();
            teacherId = Math.toIntExact(liveEntity.getTeacherId());
        }


        // 更新学生的积分
        stuInfoService.updatePoint(stuId, payPrice.multiply(pointPercent.multiply(new BigDecimal("0.01"))));
        // 更新教师的抽成
        teacherInfoService.updateCommistion(teacherId, payPrice.multiply(commisionPercent.multiply(new BigDecimal("0.01"))));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String orderIfExist(Long userId, Integer courseId, Integer courseType) {


        List<CourseOrderEntity> courseOrderEntities = this.getBaseMapper().queryOrderIfExist(userId, courseId, courseType);
        if (courseOrderEntities == null || courseOrderEntities.size() == 0) {
            return null;
        } else {


            if (courseOrderEntities.size() > 1) {
                throw new RuntimeException("出现重复购买，请联系管理员进行解决");
            }

            getBaseMapper().updateShowStatus(courseOrderEntities.get(0).getId());
            return "<script>\n" +
                    "\n" +
                    "    alert(\"当前课程的订单信息已存在，请在个人中心中查看\");\n" +
                    "    window.history.back(); location.reload();\n" +
                    "</script>";
        }


    }



}


