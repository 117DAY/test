package com.mnnu.examine.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
@Data
@TableName("gwy_propaganda")
public class PropagandaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;
    /**
     * 轮播图的url
     */
    private String picUrl;
    /**
     * 逻辑删除位【0删除，1存在】
     */
    @TableLogic
    @JsonIgnore
    private Integer showStatus;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 是否在前台展示
     */
    private Integer isShow;
    /**
     * 排序字段
     */
    private Integer sort;

}
