package io.renren.modules.exam.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 试卷分类
 *
 * @author qiaoh
 */
@Data
public class ExamCategoryVO implements Serializable {

    private Long id;
    /**
     * 试卷分类名
     */
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
     * 排序字段
     */
    private Integer sort;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 子分类
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ExamCategoryVO> children = new ArrayList<>();

}
