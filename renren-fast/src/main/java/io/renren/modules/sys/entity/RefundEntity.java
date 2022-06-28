package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:47
 */
@Data
@TableName("gwy_refund")
public class RefundEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 申请退款的用户id
	 */
	private Long userId;
	/**
	 * 支付生成的流水号
	 */
	private String serialNum;
	/**
	 * 课程id
	 */
	private Integer courseId;
	/**
	 * 支付时的价格
	 */
	private BigDecimal payPrice;
	/**
	 * 操作退款的管理员姓名
	 */
	private String operator;
	/**
	 * 退款状态【0申请退款，1同意退款，2拒绝退款】
	 */
	private Integer status;
	/**
	 * 操作员给用户退款的截图证明
	 */
	private String refundScreenshots;
	/**
	 * 管理员备注
	 */
	private String remark;
	/**
	 * 课程类型
	 */
	private Integer courseType;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 申请时间
	 */
	private String createTime;
	/**
	 * 退款处理时间
	 */
	private String finishTime;
	/**
	 * 课程标题
	 */
	private String courseName;
	/**
	 * 用户手机
	 */
	private String userPhone;
	/**
	 * 购买课程日期
	 */
	private String purchasingDate;
	/**
	 * 自定义订单号
	 */
	private String orderId;
	/**
	 * 支付方式
	 */
	private String payMode;
}
