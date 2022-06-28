package io.renren.modules.question.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:57
 */
@Data
@TableName("gwy_question")
public class QuestionEntity{
    /**
     * 试题id
     */
    private Integer id;

    /**
     * 试题标题
     */
    @NotBlank(message = "题文内容不能为空!")
    private String questionTopic;

    /**
     * 试题选项
     */
    @NotBlank(message = "选项内容不能为空!")
    private String choice;

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
     * 逻辑删除位【0删除，1存在】
     */
    @JsonIgnore
    private Integer showStatus;

    /**
     * 解析
     */
    @NotBlank(message = "解析内容不能为空!")
    private String analysis;

    /**
     * 分数
     */
    @NotNull(message = "题目分数不能为空!")
    private BigDecimal score;

    /**
     * 是否为自定义试题【0为管理员上传，1为自定义】
     */
    private Integer isCustom;

    /**
     * 试卷id
     */
    private Long examId;

    /**
     * 试题列表
     */
    @TableField(exist = false)
    private List<TextEntity> textList;

    /**
     * 试题分类路径
     */
    @TableField(exist = false)
    private List<Integer> path;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

}
