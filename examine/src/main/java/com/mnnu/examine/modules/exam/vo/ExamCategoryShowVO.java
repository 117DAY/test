package com.mnnu.examine.modules.exam.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于展示前端试卷分类数据的VO
 */
@Data
public class ExamCategoryShowVO implements Serializable {
    /**
     * 描述
     */
    private String direction;

    /**
     * 分类列表
     */
    private List<Object> data = new ArrayList<>();


}
