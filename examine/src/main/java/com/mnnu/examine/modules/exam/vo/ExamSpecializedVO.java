package com.mnnu.examine.modules.exam.vo;

import lombok.Data;

import java.util.List;

@Data
public class ExamSpecializedVO {
    /**
     * 试题数量
     */
    private Integer num;

    /**
     * 试题分类id
     */
    private Integer questionTypeId;

    /**
     * 试题分类名
     */
    private String questionTypeName;

    /**
     * 试卷分类
     */
    private List<Integer> categoryIds;

    /**
     * 试卷类型
     */
    private Integer typeId;
}
