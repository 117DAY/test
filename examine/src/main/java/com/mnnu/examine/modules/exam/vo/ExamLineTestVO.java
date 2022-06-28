package com.mnnu.examine.modules.exam.vo;


import com.mnnu.examine.modules.question.vo.QuestionLineTestVO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于封装行测试卷以及试题
 *
 * @author jljy
 */
@Data
public class ExamLineTestVO {

    /**
     * 试卷id
     */
    private Long id;

    /**
     * 试卷名称
     */
    @NotBlank
    private String examTitle;

    /**
     * 试卷分类id
     */
    @NotNull
    private Integer categoryId;

    /**
     * 试卷类型(行测1，申论2)
     */
    private Integer type;

    /**
     * 是否为自定义试卷【0为管理员上传，1为自定义】
     */
    private Integer isCustom;

    /**
     * 考试时限(单位:minute)
     */
    @NotNull
    private Integer limitTime;

    /**
     * 总分
     */
    private BigDecimal totalScore;

    /**
     * 试卷上传路径
     */
    private String path;


    /**
     * 分类后的试题VO对象数组
     */
    private List<QuestionLineTestVO> groupedQuestions = new ArrayList<>();


}
