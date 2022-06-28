package io.renren.modules.exam.vo;

import io.renren.modules.question.entity.QuestionEntity;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用于封装(面试)试卷以及试题
 *
 * @author jljy
 */
@Data
public class ExamInterViewVO {

    private Long id;

    /**
     * 试卷标题
     */
    @NotBlank(message = "试卷标题不能为空!")
    private String examTitle;

    /**
     * 试卷分类id
     */
    @NotNull(message = "试卷分类不能为空!")
    private Integer categoryId;

    /**
     * 试卷类型【1行测，2申论，3面试】
     */
    private Integer type;
    /**
     * 考试时间，单位分钟
     */
    @NotNull(message = "考试时限不能为空!")
    private Integer limitTime;

    /**
     * 试卷上传路径
     */
    private String path;

    /**
     * 试题列表
     */
    @NotEmpty(message = "试题列表不能为空!")
    private List<QuestionEntity> questions;

}
