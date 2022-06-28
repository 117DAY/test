package io.renren.modules.sys.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 样式视图,传入css样式，并设置时间，然后就能自动转换为网站原本的样子
 * 不需要再手动转换
 *
 * @author qiaoh
 * @date 2021/12/19
 */
@Data
public class StyleVO {
    /**
     * 风格
     */
    @NotNull
    String style;
    /**
     * 到期时间
     * -1 为不过期
     */
    @NotNull
    Long expireTime;
}
