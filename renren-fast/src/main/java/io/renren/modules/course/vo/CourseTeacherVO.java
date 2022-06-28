package io.renren.modules.course.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ——Reborn
 */
@Data

public class CourseTeacherVO {
    private static final long serialVersionUID = 1L;

    /**
     *
     */

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
     * 逻辑删除位【0删除，1存在】
     */
    private Integer showStatus;
    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否公开，【0不公开，1公开】
     */
    private Integer isPublic;


    /**
     * 老师头像
     */
    private String headPortrait;

    /**
     * 老师姓名
     */
    private String username;

}
