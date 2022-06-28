package com.mnnu.examine.modules.question.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:57
 */
@Data
@TableName("gwy_question_type")
public class QuestionTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 *
	 */
	private String typeName;
	/**
	 * 父节点的id
	 */
	private Integer parentId;
	/**
	 * 树形结构的层级默认是0
	 */
	private Integer typeLevel;
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
	 * 子分类
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private List<QuestionTypeEntity> children = new ArrayList<>();

}
