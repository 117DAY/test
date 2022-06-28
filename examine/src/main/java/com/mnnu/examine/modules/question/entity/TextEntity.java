package com.mnnu.examine.modules.question.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * (申论)试题材料表
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-01 22:30:37
 */
@Data
@TableName("gwy_text")
public class TextEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;

    /**
     * 材料标签值(对应前端)
     */
    @TableField(exist = false)
    private String name;

    /**
     * 材料内容
     */
    private String textContent;
    /**
     * 排序字段
     */
    private Integer sort;

	/**
	 * 材料列表
	 */
	@TableField(exist = false)
    private List<QuestionEntity> questionList;
}
