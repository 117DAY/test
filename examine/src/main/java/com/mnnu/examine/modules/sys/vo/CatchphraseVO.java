package com.mnnu.examine.modules.sys.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CatchphraseVO {

    private Integer id;

    /**
     * 音乐地址
     */
    private String url;
}
