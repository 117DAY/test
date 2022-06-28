package com.mnnu.examine.modules.question.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 材料和(申论)试题关系表
 *
 * @author 自动生成
 * @email generate
 * @date 2021-12-01 22:30:37
 */
@Data
@TableName("gwy_question_text_relation")
public class QuestionTextRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 * (申论)试题id
	 */
	private Integer questionId;
	/**
	 * 材料id
	 */
	private Integer questionTextId;

}
