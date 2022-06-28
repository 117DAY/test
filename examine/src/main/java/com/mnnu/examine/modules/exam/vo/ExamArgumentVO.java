package com.mnnu.examine.modules.exam.vo;


import com.mnnu.examine.modules.question.entity.TextEntity;
import com.mnnu.examine.modules.question.vo.QuestionArgumentVO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于封装申论试卷以及问题
 *
 * @author jljy
 */
@Data
public class ExamArgumentVO implements Serializable {

    /**
     * 试卷id
     */
    private Long id;

    /**
     * 试卷标题
     */
    @NotBlank
    private String examTitle;

    /**
     * 试卷分类的id
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
     * 所有材料
     */
    private List<TextEntity> texts = new ArrayList<>();

    /**
     * 所有问题
     */
    private List<QuestionArgumentVO> questions = new ArrayList<>();





}
