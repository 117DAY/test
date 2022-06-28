package com.mnnu.examine.modules.exam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.AccessType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 试卷分类
 *
 * @author qiaoh
 */
@Data
@Accessors(chain = true)
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
     * 唯一id
     */
    private String uuid;

    /**
     * 子分类
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ExamCategoryVO> children = new ArrayList<>();

}
