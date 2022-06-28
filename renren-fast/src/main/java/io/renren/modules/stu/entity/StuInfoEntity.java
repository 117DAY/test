package io.renren.modules.stu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
@Data
@TableName("gwy_stu_info")
public class StuInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 学生id
	 */
	private Long stuId;
	/**
	 * 积分余额
	 */
	private BigDecimal point;

}
