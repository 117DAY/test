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
 * @date 2021-11-10 16:49:05
 */
@Data
@TableName("gwy_catchphrase")
public class CatchphraseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 心灵鸡汤内容
	 */
	private String words;

}
