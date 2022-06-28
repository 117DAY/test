package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * qqservice实体
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
@Data
@TableName("gwy_service")
public class QQServiceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;
    /**
     * qq客服的超链接
     */
    private String qqLink;
    /**
     * 删除状态【0删除，1存在】
     */
    @TableLogic
    @JsonIgnore
    private Integer showStatus;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 客服姓名
     */
    private String name;
    /**
     * 是否启用【0禁用，1启用】
     */
    private Integer status;
    /**
     * 客服手机号
     */
    private String phone;

}
