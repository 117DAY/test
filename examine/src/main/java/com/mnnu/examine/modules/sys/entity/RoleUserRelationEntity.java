package com.mnnu.examine.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
@Data
@TableName("gwy_role_user_relation")
public class RoleUserRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Integer roleId;
	/**
	 * 
	 */
	private Long userId;

}
