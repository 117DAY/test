package io.renren.modules.exam.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.renren.modules.question.vo.QuestionLineTestVO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
    @NotBlank(message = "试卷标题不能为空!")
    private String examTitle;

    /**
     * 试卷分类id
     */
    @NotNull(message = "试卷分类不能为空!")
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
    @NotNull(message = "考试时限不能为空!")
    private Integer limitTime;

    /**
     * 总分
     */
    @NotNull(message = "试卷总分不能为空!")
    private BigDecimal totalScore;

    /**
     * 试卷上传地址
     */
    private String path;


    /**
     * 分类后的试题VO对象数组
     */
    @NotEmpty(message = "大题内容不能为空!")
    @Valid
    private List<QuestionLineTestVO> groupedQuestions = new ArrayList<>();


}
