package io.renren.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:57
 */
@Data
@TableName("gwy_question_record")
public class QuestionRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 题目的id
	 */
	private Long questionId;
	/**
	 * 试卷记录的id
	 */
	private Long recordId;
	/**
	 * 回答，如果是选项则用'\n'分割
	 */
	private String reply;
	/**
	 * 得分
	 */
	private BigDecimal score;

}
