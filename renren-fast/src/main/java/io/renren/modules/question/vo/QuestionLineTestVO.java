package io.renren.modules.question.vo;


import io.renren.modules.question.entity.QuestionEntity;
import io.renren.modules.question.entity.QuestionTypeEntity;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于封装试题以及试题类型
 *
 * @author jljy
 */
@Accessors(chain = true)
@Data
public class QuestionLineTestVO {
    /**
     * 分数
     */
    @NotNull(message = "大题分数不能为空!")
    private BigDecimal score;

    /**
     * 试题根类型名称
     */
    @NotBlank(message = "大题名不能为空!")
    private String rootName;

    /**
     * 试题类型树
     */
    private List<QuestionTypeEntity> typeList;

    /**
     * 默认路径
     */
    private List<Integer> defaultPath;

    /**
     * 试题分类下的试题列表
     */
    @NotEmpty
    @Valid
    private List<QuestionEntity> questions = new ArrayList<>();

}
