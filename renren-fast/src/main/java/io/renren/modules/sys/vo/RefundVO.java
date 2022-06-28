package io.renren.modules.sys.vo;

import lombok.Data;

import java.util.List;

/**
 * 退款页面视图
 *
 * @author qiaoh
 * @date 2021/12/18
 */
@Data
public class RefundVO {
    /**
     * 照片
     */
    List<String> photos;
    /**
     * 同意
     */
    Integer agree;
    /**
     * 订单id
     */
    String orderId;
    /**
     * 评价
     */
    String mark;
}
