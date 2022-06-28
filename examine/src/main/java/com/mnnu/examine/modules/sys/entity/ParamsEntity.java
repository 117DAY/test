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
 * @date 2021-12-05 21:42:47
 */
@Data
@TableName("gwy_params")
public class ParamsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 显示在页脚的一些信息，不同的信息用'|'分割
	 */
	private String params;
	/**
	 * 免责声明
	 */
	private String disclaimer;
	/**
	 * 用户需知
	 */
	private String userAgreement;

}
