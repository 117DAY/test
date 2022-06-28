package com.mnnu.examine.modules.exam.vo;


import com.mnnu.examine.modules.question.entity.QuestionEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
    @NotBlank
    private String examTitle;

    /**
     * 试卷分类id
     */
    @NotNull
    private Integer categoryId;

    /**
     * 试卷类型【1行测，2申论，3面试】
     */
    private Integer type;
    /**
     * 考试时间，单位分钟
     */
    @NotNull
    private Integer limitTime;

    /**
     * 试卷上传路径
     */
    private String path;

    /**
     * 试题列表
     */
    private List<QuestionEntity> questions;

}
