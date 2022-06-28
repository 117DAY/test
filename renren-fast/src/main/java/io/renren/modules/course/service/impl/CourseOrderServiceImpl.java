package io.renren.modules.course.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.GwyConstant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.course.dao.CourseOrderDao;
import io.renren.modules.course.entity.CourseEntity;
import io.renren.modules.course.entity.CourseOrderEntity;
import io.renren.modules.course.service.CourseOrderService;
import io.renren.modules.course.service.CourseService;
import io.renren.modules.course.vo.CourseOrderUserVO;
import io.renren.modules.live.entity.LiveEntity;
import io.renren.modules.live.service.LiveService;
import io.renren.modules.sys.entity.GwyUserEntity;
import io.renren.modules.sys.service.GwyUserService;
import io.renren.modules.teacher.entity.TeacherInfoEntity;
import io.renren.modules.teacher.service.TeacherInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("courseOrderService")
public class CourseOrderServiceImpl extends ServiceImpl<CourseOrderDao, CourseOrderEntity> implements CourseOrderService {

    @Autowired
    CourseService courseService;
    @Autowired
    LiveService liveService;
    @Autowired
    TeacherInfoService teacherInfoService;
    @Autowired
    GwyUserService gwyUserService;

    /**
     * status 订单状态0新建未支付、1已支付、2申请退款、3已退款
     * orderId 订单号
     * payMode 微信或支付宝
     * courseId 课程id
     * courseType 课程类型
     * <p>
     * 列表
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<CourseOrderEntity> wrapper = new QueryWrapper<>();
        String status = (String) params.get("status");
        String orderId = (String) params.get("orderId");
        String payMode = (String) params.get("payMode");
        Long courseId = (Long) params.get("courseId");
        String serial_num = (String) params.get("serialNum");
        String courseType1 = (String) params.get("courseType");


        if (StringUtils.isNotBlank(status)) {
            Integer integer = Integer.valueOf(status);
            wrapper.and(w -> w.eq("status", integer));
        }
        if (StringUtils.isNotBlank(serial_num)) {
            wrapper.and(w -> w.eq("serial_num", serial_num));
        }
        if (StringUtils.isNotBlank(orderId)) {
            wrapper.and(w -> w.like("order_id", orderId));
        }


        if (StringUtils.isNotBlank(payMode)) {
            wrapper.and(w -> w.eq("pay_mode", payMode));
        }

        if (courseId != null && courseId > 0L) {
            wrapper.and(w -> w.eq("course_id", courseId));
        }
        if (StringUtils.isNotBlank(courseType1)) {
            Integer courseType = Integer.valueOf(courseType1);
            boolean isLegalType = (courseType.equals(GwyConstant.CourseType.RECORDED.getType()) || courseType.equals(GwyConstant.CourseType.LIVE.getType()));
            if (isLegalType) {
                wrapper.and(w -> w.eq("course_type", courseType));
            }
        }
        IPage<CourseOrderEntity> page = this.page(
                new Query<CourseOrderEntity>().getPage(params), wrapper
        );

        ArrayList<CourseOrderUserVO> vos = new ArrayList<>();
        List<CourseOrderEntity> courseOrderEntities = page.getRecords();

        for (CourseOrderEntity courseOrderEntity : courseOrderEntities) {
            CourseOrderUserVO vo = new CourseOrderUserVO();
            //订单对象赋值vo
            BeanUtils.copyProperties(courseOrderEntity, vo);
            //用户名
            GwyUserEntity userEntity = gwyUserService.getBaseMapper().selectOne(new QueryWrapper<GwyUserEntity>().eq("id", courseOrderEntity.getUserId()));
            //  用户名
            vo.setUsername(userEntity.getUsername());
            //  手机
            vo.setPhone(userEntity.getPhone());
            Long courseId1 = courseOrderEntity.getCourseId();
            Integer courseType = courseOrderEntity.getCourseType();

            String courseTitle = "";
            BigDecimal currentPrice = null;
            Integer teacherId = null;
            BigDecimal orginalPrice = null;
            if (courseType != null && Objects.equals(courseType, GwyConstant.CourseType.RECORDED.getType())) {
                CourseEntity courseEntity = courseService.getBaseMapper().selectOne(new QueryWrapper<CourseEntity>().eq("id", courseId1));
                courseTitle = courseEntity.getCourseTitle();
                currentPrice = courseEntity.getCurrentPrice();
                teacherId = courseEntity.getTeacherId();
                orginalPrice = courseEntity.getOrginalPrice();
            } else {
                LiveEntity liveEntity = liveService.getById(courseId1);
                courseTitle = liveEntity.getCourseTitle();
                currentPrice = liveEntity.getCurrentPrice();
                teacherId = liveEntity.getTeacherId().intValue();
                orginalPrice = liveEntity.getOrginalPrice();
            }
            // 课程名字
            vo.setCourseTitle(courseTitle);
            // 课程现在价格
            vo.setCurrentPrice(currentPrice);
            // 老师id
            vo.setTeacherId(teacherId);
            // 原价
            vo.setOrginalPrice(orginalPrice);
            // 老师真实姓名
            TeacherInfoEntity teacher = teacherInfoService.getBaseMapper().selectOne(new QueryWrapper<TeacherInfoEntity>().eq("teacher_id", teacherId));
            vo.setRealName(teacher.getRealName());

            vos.add(vo);
        }


        return new PageUtils(vos, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }


    /**
     * @param params limit
     * @param params page
     * @param params type(0-普通课程,1-直播课程,3-订单信息)
     * @description: 根据用户ID获取用户已购买课程列表 (或订单信息)
     * @return: 课课程分页
     * @author: Geralt
     * @time: 2021/11/26 17:44
     */
    @Override
    public PageUtils getCourseListByUserId(Map<String, Object> params, Long userId) {
        int type = Integer.parseInt((String) params.get("type"));
        QueryWrapper<CourseOrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("course_type", type);
        IPage<CourseOrderEntity> page = this.page(
                new Query<CourseOrderEntity>().getPage(params), wrapper
        );
        PageUtils orderPage = new PageUtils(page);
        List<CourseOrderEntity> orderList = (List<CourseOrderEntity>) orderPage.getList();
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
                .eq("course_id", liveId);
        return getBaseMapper().selectCount(queryWrapper) >= 1;
    }

}
