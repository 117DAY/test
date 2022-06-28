package io.renren.modules.course.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("gwy_course")
public class CourseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;
    /**
     * 课程标题
     */
    private String courseTitle;
    /**
     * 原价，六位数，两位小数
     */
    private BigDecimal orginalPrice;
    /**
     * 课程介绍
     */
    private String details;
    /**
     * 课程开始时间
     */
    private String beginTime;
    /**
     * 教师的id
     */
    private Integer teacherId;
    /**
     * 封面url
     */
    private String coverUrl;
    /**
     * 课程现价
     */
    private BigDecimal currentPrice;
    /**
     * 教师返还比例，默认0
     */
    private BigDecimal commisionPercent;
    /**
     * 学生购买后赠送的积分比例
     */
    private BigDecimal pointPercent;
    /**
     * 逻辑删除位【0删除，1存在】
     */
    @JsonIgnore
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
     * 是否公开，【0不公开，1公开】
     */
    private Integer isPublic;
    /**
     * 课程视频的地址，如果有多个用'\n'隔开
     */
    private String videoUrl;

    /**
     * 课程类型
     */
    private Integer courseType;

}
