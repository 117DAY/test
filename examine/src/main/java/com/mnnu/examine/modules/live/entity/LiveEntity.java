package com.mnnu.examine.modules.live.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
@Data
@TableName("gwy_live")
public class LiveEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 课程标题
     */
    private String courseTitle;
    /**
     * 课程原价，六位数，两位小数
     */
    private BigDecimal orginalPrice;
    /**
     * 课程现价，六位数，两位小数
     */
    private BigDecimal currentPrice;
    /**
     * 封面图片的地址
     */
    private String coverUrl;
    /**
     * 教师id
     */
    private Long teacherId;
    /**
     * 课程描述
     */
    private String details;
    /**
     * 直播课开始时间，开始时间前30分钟提醒
     */
    private String beginTime;
    /**
     * 直播课结束时间
     */
    private String finishTime;
    /**
     * 逻辑删除位【0删除，1存在】
     */
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
     * 视频url地址
     */
    private String videoUrl;

    /**
     * 是否公开
     */
    private Integer isPublic;


    /**
     * 教师返还比例，默认0
     */
    private BigDecimal commisionPercent;
    /**
     * 学生购买后赠送的积分比例
     */
    private BigDecimal pointPercent;

    /*
    * 保利威频道ID
    * */
    private String channelId;
    /*
     * 保利威频道Key
     * */
    private String secretKey;
}
