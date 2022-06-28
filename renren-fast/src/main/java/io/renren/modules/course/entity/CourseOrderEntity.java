package io.renren.modules.course.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
@Data
@TableName("gwy_course_order")
public class CourseOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 支付方式【'微信','支付宝'】
     */
    private String payMode;
    /**
     * 支付公司方提供的流水号
     */
    private String serialNum;
    /**
     * 订单状态【0新建未支付，1已支付，2申请退款，3已退款】
     */
    private Integer status;
    /**
     * 课程id
     */
    private Long courseId;
    /**
     * 课程类型【0录播课，1直播课】
     */
    private Integer courseType;
    /**
     * 支付价格
     */
    private BigDecimal payPrice;
    /**
     * 逻辑删除位【0删除，1存在】
     */
//    @JsonIgnore
//    private Integer showStatus;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 自定义订单id
     */
    private String orderId;

}
