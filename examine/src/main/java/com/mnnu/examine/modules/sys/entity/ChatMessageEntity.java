package com.mnnu.examine.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-12-09 16:28:24
 */
@Data
@TableName("gwy_chat_message")
public class ChatMessageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 接受者的id
     */
    private Long toUser;
    /**
     * 发送者的id
     */
    private Long fromUser;
    /**
     * 信息内容
     */
    private String content;
    /**
     * 逻辑删除位
     */
    @JsonIgnore
    private Integer showStatus;
    /**
     * 发送时间
     */
    private String sendTime;

}
