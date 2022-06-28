package io.renren.modules.sys.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatchphraseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String url;
    private int index;

}
