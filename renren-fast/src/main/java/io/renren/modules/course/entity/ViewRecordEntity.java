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
 * @date 2021-11-19 14:21:23
 */
@Data
@TableName("gwy_view_record")
public class ViewRecordEntity implements Serializable {
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
     * 课程id
     */
    private Long courseId;
    /**
     * 课程种类
     */
    private Long courseType;
    /**
     * 观看的时间 年月日
     */
    private String viewTime;
    /**
     * 逻辑删除位
     */
    @JsonIgnore
    @TableLogic
    private Integer show_status;
    /**
     * 观看视频的百分比
     */
    private BigDecimal viewPercentage;
    /**
     * 观看视频到第几秒数
     */
    private String viewSecond;
    /**
     * 课程封面
     */
    private String courseCoverUrl;
    /**
     * 课程名字
     */
    private String courseTitle;

}
