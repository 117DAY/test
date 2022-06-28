package com.mnnu.examine.modules.question.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:57
 */
@Data
@TableName("gwy_question")
public class QuestionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 题文
     */
    private String questionTopic;
    /**
     * 选项，用'\n'分割
     */
    private String choice;
    /**
     * 答案，用'\n'分割
     */
    private String answer;
    /**
     * 题库类型的id
     */
    private Integer typeId;
    /**
     * 排序字段，默认0
     */
    private Integer sort;
    /**
     * 是否为自定义试题【0为管理员上传，1为自定义】
     */
    private Integer isCustom;
    /**
     * 逻辑删除位【0删除，1存在】
     */
    @TableLogic
    @JsonIgnore
    private Integer showStatus;
    /**
     * 题目解析
     */
    private String analysis;
    /**
     * 分值，两位小数
     */
    private BigDecimal score;
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
    /**
     * 考卷id
     */
    private Long examId;

    /**
     * 试题列表
     */
    @TableField(exist = false)
    private List<TextEntity> textList;

    /**
     * 试题分类路径
     */
    @TableField(exist = false)
    private List<Object> path;

}
