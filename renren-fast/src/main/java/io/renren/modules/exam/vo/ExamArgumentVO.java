package io.renren.modules.exam.vo;

import io.renren.modules.question.entity.TextEntity;
import io.renren.modules.question.vo.QuestionArgumentVO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotBlank(message = "试卷标题不能为空!")
    private String examTitle;

    /**
     * 试卷分类的id
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
    @NotNull(message = "考试时限不能为空")
    private Integer limitTime;

    /**
     * 试卷上传路径
     */
    private String path;

    /**
     * 所有材料
     */
    @NotEmpty(message = "材料不能为空!")
    @Valid
    private List<TextEntity> texts = new ArrayList<>();

    /**
     * 所有问题
     */
    @NotEmpty(message = "问题不能为空!")
    @Valid
    private List<QuestionArgumentVO> questions = new ArrayList<>();


}
