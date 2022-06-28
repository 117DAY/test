package com.mnnu.examine.modules.question.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于封装(申论)试题列表
 * @author jljy
 */
@Accessors(chain = true)
@Data
public class QuestionArgumentVO {
    /**
     * 试题id
     */
    private Integer id;

    /**
     * 对应前端字段
     */
    private String name;

    /**
     * 试题标题
     */
    private String questionTopic;

    /**
     * 答案
     */
    private String answer;

    /**
     * 试题类型id
     */
    private Integer typeId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 试题类型到根节点的路径
     */
    private List<Object> path;

    /**
     * 对应材料列表的name属性
     */
    private List<String> textList = new ArrayList<>();
}
