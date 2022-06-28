package io.renren.modules.live.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LiveWithTeacherVO {
    private static final long serialVersionUID = 1L;

    /**
     *
     */

    private Long id;
    /**
     * 课程标题
     */
    private String courseTitle;

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
    private Long teacherId;
    /**
     * 封面url
     */
    private String coverUrl;
    /**
     * 课程现价
     */
    private BigDecimal currentPrice;
    /**
     * 原价，六位数，两位小数
     */
    private BigDecimal orginalPrice;
    /**
     * 课程视频的地址，如果有多个用'\n'隔开
     */
    private String videoUrl;

    /*
     * 老师头像
     * */
    private String headPortrait;

    /*
     * 老师头像
     * */
    private String username;

}
