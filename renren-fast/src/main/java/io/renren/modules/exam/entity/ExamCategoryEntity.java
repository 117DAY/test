package io.renren.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:57
 */
@Data
@TableName("gwy_exam_category")
public class ExamCategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 试卷分类名
     */
    @NotBlank(message = "分类名称不能为空!")
    private String categoryName;
    /**
     * 父类id
     */
    private Long parentId;
    /**
     * 分类等级
     */
    private Integer categoryLevel;
    /**
     * 逻辑删除位【0删除，1存在】
     */
    @TableLogic
    @JsonIgnore
    private Integer showStatus;
    /**
     * 排序字段
     */
    private Integer sort;
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
