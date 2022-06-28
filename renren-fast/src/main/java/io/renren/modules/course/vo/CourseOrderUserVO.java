package io.renren.modules.course.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CourseOrderUserVO {
//    *********订单表
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
    @JsonIgnore
    @TableLogic
    private Integer showStatus;
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

//   ********** 课程表

    /**
     * 课程标题
     */
    private String courseTitle;
    /**
     * 原价，六位数，两位小数
     */
    private BigDecimal orginalPrice;
    /**
     * 课程现价
     */
    private BigDecimal currentPrice;
    /**
     * 学生购买后赠送的积分比例
     */
    private BigDecimal pointPercent;
    /**
     * 课程视频的地址，如果有多个用'\n'隔开
     */
    private String videoUrl;

//    用户表

    /**
     * 用户手机号
     */

    private String phone;

    /**
     * 用户名
     */
    private String username;
    /**
     * 性别【0男性，1女性】
     */
    private Integer gender;

    //*************老师表

    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 老师id
     */
    private Integer teacherId;
}
