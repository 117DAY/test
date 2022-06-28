package com.mnnu.examine.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 
 * 
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
@Data
@TableName("gwy_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 用户手机号
	 */

	private String phone;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 性别【0男性，1女性】
	 */
	private Integer gender;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 微信的id
	 */
	private String openid;
	/**
	 * 出生年份
	 */
	private String birthYear;
	/**
	 * 逻辑删除位【0删除，1存在】
	 */
	@JsonIgnore
	private Integer showStatus;
	/**
	 * 注册时间
	 */
	private String createTime;
	/**
	 * 修改个人信息时间
	 */
	private String updateTime;
	/**
	 * 头像
	 */
	private String headPortrait;
	/**
	 * 最后一次登录时间
	 */
	private String lastLoginTime;
	/**
	 * 最后一次登录ip
	 */
	private String lastLoginIp;
	/**
	 * 个人简介
	 */
	private String personalIntroduction;
	/**
	 * 个人签名，类似qq个签
	 */
	private String motto;

}
