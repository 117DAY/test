package com.mnnu.examine.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("gwy_exam")
public class ExamEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 题目标题
     */
    private String examTitle;
    /**
     * 试卷分类的id
     */
    private Integer categoryId;
    /**
     * 试卷类型【0行测，1申论，2面试】
     */
    private Integer type;
    /**
     * 考试时间，单位分钟
     */
    private Integer limitTime;
    /**
     * 是否为自定义试卷【0为管理员上传，1为自定义】
     */
    private Integer isCustom;
    /**
     * 考试总分
     */
    private BigDecimal totalScore;
    /**
     * 试卷上传路径
     */
    private String path;
    /**
     * 逻辑删除位【0删除，1存在】
     */
    @TableLogic
    @JsonIgnore
    private Integer showStatus;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

}
