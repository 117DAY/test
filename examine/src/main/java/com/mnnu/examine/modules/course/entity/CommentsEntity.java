package com.mnnu.examine.modules.course.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author 自动生成
 * @email generat
 * @date 2021-11-25 15:55:18
 */
@Data
@TableName("gwy_comments")
public class CommentsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 课程的id
	 */
	private Long courseId;
	/**
	 * 课程的类型
	 */
	private Integer courseType;
	/**
	 * 评论者的id
	 */
	private Long commentId;
	/**
	 * 被回复者的id，如果没有则为0
	 */
	private Long replyId;
	/**
	 * 评论者的名字
	 */
	private String commentName;
	/**
	 * 回复者的名字
	 */
	private String replyName;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * 评论时间
	 */
	private String time;
	/**
	 * 逻辑删除位
	 */
	private Integer showStatus;
	/**
	 * 是否显示该条评论
	 */
	private Integer isPublic;

	/**
	 * 根的评论
	 */
	private Long rootComment;

}
