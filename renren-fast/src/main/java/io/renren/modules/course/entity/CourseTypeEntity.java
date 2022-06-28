package io.renren.modules.course.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-26 10:24:40
 */
@Data
@TableName("gwy_course_type")
public class CourseTypeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 父类的id，如果没有则为空
     */
    private Long parentId;
    /**
     * 分类结点所在的等级
     */
    private Integer typeLevel;
    /**
     * 逻辑删除字段
     */
    @JsonIgnore
    private Integer showStatus;
    /**
     * 排序字段
     */
    private Integer sort;

}
