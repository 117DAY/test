package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.RefundEntity;

import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:47
 */
public interface RefundService extends IService<RefundEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 处理退款
     *
     * @param refund 退款
     * @param agree  同意
     */
    void dealRefund(RefundEntity refund, Integer agree);

    public boolean getOrderInfo(Map<String, Object> params);

    RefundEntity getRefundByOrderId(String orderId);

}

