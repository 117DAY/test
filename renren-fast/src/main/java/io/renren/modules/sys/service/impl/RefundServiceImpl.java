package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.GwyConstant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.course.entity.CourseOrderEntity;
import io.renren.modules.course.service.CourseOrderService;
import io.renren.modules.stu.service.StuInfoService;
import io.renren.modules.sys.dao.RefundDao;
import io.renren.modules.sys.entity.RefundEntity;
import io.renren.modules.sys.service.RefundService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;


@Service("refundService")
public class RefundServiceImpl extends ServiceImpl<RefundDao, RefundEntity> implements RefundService {

    @Resource
    CourseOrderService courseOrderService;

    @Resource
    StuInfoService stuInfoService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RefundEntity> page = this.page(
                new Query<RefundEntity>().getPage(params),
                new QueryWrapper<RefundEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 交易退款
     * agree 1 同意 2 拒绝
     *
     * @param refund 退款
     * @param agree  同意
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealRefund(RefundEntity refund, Integer agree) {
        Integer orderStatus = 2;
        switch (agree) {
            case 1:
                orderStatus = GwyConstant.OrderStatus.REFUNDED.getCode();
                break;
            case 2:
                orderStatus = GwyConstant.OrderStatus.PAID.getCode();
                break;
            default:
                break;
        }
        // 更新退款状态
        this.update(refund, new QueryWrapper<RefundEntity>().eq("order_id", refund.getOrderId()));

        // 更新订单状态
        courseOrderService.update(new UpdateWrapper<CourseOrderEntity>()
                .set("status", orderStatus)
                .eq("order_id", refund.getOrderId()));

        // 扣除用户积分

        RefundEntity refundEntity = this.getOne(new QueryWrapper<RefundEntity>().eq("order_id", refund.getOrderId()));
        Long userId = refundEntity.getUserId();
        stuInfoService.subtractPoint(refundEntity.getCourseId(), refundEntity.getCourseType(), refundEntity.getUserId(), refundEntity.getPayPrice());
    }

    @Override
    public boolean getOrderInfo(Map<String, Object> params) {
        return false;
    }

    @Override
    public RefundEntity getRefundByOrderId(String orderId) {
        return getBaseMapper().selectOne(new QueryWrapper<RefundEntity>().eq("order_id", orderId));
    }

}
