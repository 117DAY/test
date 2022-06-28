package io.renren.modules.exam.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ExamRecordVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
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
     * 花费时间
     */
    private String expendTime;
    /**
     * 答案列表
     */
    private List<String> userAnswers;

    /**
     * 提交时间
     */
    private String commitTime;

    /**
     * 本次做题是否已批改 1已批改 0未批改
     */
    private Integer isChecked;
}
