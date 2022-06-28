package com.mnnu.examine.modules.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 消息视图对象
 *
 * @author qiaoh
 * @date 2021/12/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageVO {
    /**
     * 接收方
     */
    @NotNull
    private Long toUser;

    /**
     * 内容
     */
    private String content;

    /**
     * 发送时间
     */
    @NotNull
    private String sendTime;


}
