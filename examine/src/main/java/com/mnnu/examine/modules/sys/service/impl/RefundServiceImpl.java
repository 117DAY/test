package com.mnnu.examine.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.course.entity.CourseEntity;
import com.mnnu.examine.modules.course.entity.CourseOrderEntity;
import com.mnnu.examine.modules.course.service.CourseService;
import com.mnnu.examine.modules.live.entity.LiveEntity;
import com.mnnu.examine.modules.live.service.LiveService;
import com.mnnu.examine.modules.sys.dao.RefundDao;
import com.mnnu.examine.modules.sys.entity.RefundEntity;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.RefundService;
import com.mnnu.examine.modules.sys.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


@Service("refundService")
public class RefundServiceImpl extends ServiceImpl<RefundDao, RefundEntity> implements RefundService {
    @Resource
    UserService userService;

    @Resource
    CourseService courseService;

    @Resource
    LiveService liveService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RefundEntity> page = this.page(
                new Query<RefundEntity>().getPage(params),
                new QueryWrapper<RefundEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRefundInfo(CourseOrderEntity courseOrderEntity) {
        RefundEntity refundEntity = new RefundEntity();
        BeanUtils.copyProperties(courseOrderEntity, refundEntity,
                "id", "createTime", "status");
        Integer courseId = Math.toIntExact(courseOrderEntity.getCourseId());
        Integer courseType = courseOrderEntity.getCourseType();
        String courseName = "";
        if (courseType.equals(GwyConstant.CourseType.RECORDED.getType())) {
            CourseEntity courseEntity = courseService.getById(courseOrderEntity.getCourseId());
            courseName = courseEntity.getCourseTitle();
        } else {
            LiveEntity liveEntity = liveService.getById(courseOrderEntity.getCourseId());
            courseName = liveEntity.getCourseTitle();
        }


        UserEntity userEntity = userService.getById(courseOrderEntity.getUserId());
        String username = userEntity.getUsername();
        String phone = userEntity.getPhone();

        // 填充无法对应的数据
        refundEntity.setPurchasingDate(courseOrderEntity.getUpdateTime());
        refundEntity.setStatus(GwyConstant.RefundStatus.APPLY_REFUND.getCode());
        refundEntity.setCourseName(courseName);
        refundEntity.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        refundEntity.setUsername(username);
        refundEntity.setUserPhone(phone);
        refundEntity.setCourseId(courseId);
        this.save(refundEntity);
    }

}