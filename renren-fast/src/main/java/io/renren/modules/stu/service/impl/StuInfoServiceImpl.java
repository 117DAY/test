package io.renren.modules.stu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.GwyConstant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.course.entity.CourseEntity;
import io.renren.modules.course.service.CourseService;
import io.renren.modules.live.entity.LiveEntity;
import io.renren.modules.live.service.LiveService;
import io.renren.modules.stu.dao.StuInfoDao;
import io.renren.modules.stu.entity.StuInfoEntity;
import io.renren.modules.stu.service.StuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 斯图信息服务impl
 *
 * @author qiaoh
 * @date 2021/12/18
 */
@Service("stuInfoService")
public class StuInfoServiceImpl extends ServiceImpl<StuInfoDao, StuInfoEntity> implements StuInfoService {
    @Autowired
    CourseService courseService;
    @Autowired
    LiveService liveService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StuInfoEntity> page = this.page(
                new Query<StuInfoEntity>().getPage(params),
                new QueryWrapper<StuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void updatePoint(Long stuId, BigDecimal addPoint) {
        getBaseMapper().updatePoint(stuId, addPoint);
    }

    @Override
    public void subtractPoint(Integer courseId, Integer courseType, Long userId, BigDecimal payPrice) {
        // 拿到积分比例
        BigDecimal pointPercent = null;
        if (courseType.equals(GwyConstant.CourseType.RECORDED.getType())) {
            CourseEntity courseEntity = courseService.getById(courseId);
            pointPercent = courseEntity.getPointPercent();
        } else if (courseType.equals(GwyConstant.CourseType.LIVE.getType())) {
            LiveEntity liveEntity = liveService.getById(courseId);
            pointPercent = liveEntity.getPointPercent();
        }

        // 根据比例和支付金额扣除积分
        getBaseMapper().subtractPoint(userId, payPrice.multiply(pointPercent.multiply(new BigDecimal("0.01"))));

    }

}
