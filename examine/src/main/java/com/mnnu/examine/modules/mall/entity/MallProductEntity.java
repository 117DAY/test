package com.mnnu.examine.modules.mall.entity;

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
 * @date 2021-12-05 21:42:47
 */
@Data
@TableName("gwy_mall_product")
public class MallProductEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 商品名
	 */
	private String name;
	/**
	 * 商品价格
	 */
	private BigDecimal price;
	/**
	 * 商品信息
	 */
	private String details;
	/**
	 * 商品图片
	 */
	private String pics;
	/**
	 * 逻辑删除位
	 */
	private Integer showStatus;
	/**
	 * 是否上架，1上架，0下架
	 */
	private Integer isPublic;
	/**
	 * 所剩数量
	 */
	private Integer count;

}
