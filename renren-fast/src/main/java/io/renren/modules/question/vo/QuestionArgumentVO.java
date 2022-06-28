package io.renren.modules.question.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于封装(申论)试题列表
 *
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
    @NotBlank(message = "题文内容不能为空!")
    private String questionTopic;

    /**
     * 答案
     */
    @NotBlank(message = "答案内容不能为空!")
    private String answer;

    /**
     * 试题类型id
     */
    @NotNull(message = "试题分类不能为空!")
    private Integer typeId;

    /**
     * 排序
     */
    @NotNull(message = "排序字段不能为空!")
    private Integer sort;

    /**
     * 是否为自定义试题【0为管理员上传，1为自定义】
     */
    private Integer isCustom;

    /**
     * 试题类型到根节点的路径
     */
    private List<Integer> path;

    /**
     * 对应材料列表的name属性
     */
    @NotEmpty(message = "试题列表不能为空!")
    private List<String> textList = new ArrayList<>();
}
