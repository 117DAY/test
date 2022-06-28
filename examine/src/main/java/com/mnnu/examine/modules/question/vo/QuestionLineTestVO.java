package com.mnnu.examine.modules.question.vo;



import com.mnnu.examine.modules.question.entity.QuestionEntity;
import com.mnnu.examine.modules.question.entity.QuestionTypeEntity;
import lombok.Data;
import lombok.experimental.Accessors;

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
    private BigDecimal score;

    /**
     * 试题根类型名称
     */
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
    private List<QuestionEntity> questions = new ArrayList<>();

}
