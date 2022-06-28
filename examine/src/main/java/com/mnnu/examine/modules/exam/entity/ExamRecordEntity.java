package com.mnnu.examine.modules.exam.entity;

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
 * @date 2021-11-14 19:34:57
 */
@Data
@TableName("gwy_exam_record")
public class ExamRecordEntity implements Serializable {
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
     * 试卷id
     */
    private Long examId;
    /**
     * 试卷标题
     */
    private String examTitle;
    /**
     * 本次得分
     */
    private BigDecimal score;
    /**
     * 是否为自定义试卷【0为管理员上传，1为自定义】
     */
    private Integer isCustom;
    /**
     * 逻辑删除位【0删除，1存在】
     */
    @JsonIgnore
    @TableLogic
    private Integer showStatus;

    /**
     * 本次做题是否已批改 1已批改 0未批改
     */
    private Integer isChecked;

    /**
     * 花时间
     */
    private String expendTime;
    /**
     * 用户的答案
     */
    private String userAnswers;
    /**
     * 提交时
     */
    private String commitTime;
}
