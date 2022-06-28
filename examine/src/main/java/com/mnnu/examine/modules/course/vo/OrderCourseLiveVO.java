package com.mnnu.examine.modules.course.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
/*--Geralt--*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCourseLiveVO {
    /**
     * 课程标题
     */
    private String courseTitle;
    /**
     * 支付方式【'微信','支付宝'】
     */
    private String payMode;
    /**
     * 订单状态【0新建未支付，1已支付，2申请退款，3已退款】
     */
    private Integer status;
    /**
     * 课程类型【0录播课，1直播课】
     */
    private Integer courseType;
    /**
     * 支付价格
     */
    private BigDecimal payPrice;

    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 自定义订单id
     */
    private String orderId;

}
