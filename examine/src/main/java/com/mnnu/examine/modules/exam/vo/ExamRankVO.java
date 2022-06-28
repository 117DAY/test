package com.mnnu.examine.modules.exam.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExamRankVO {

    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String headPortrait;

    /**
     * 分数
     */
    private BigDecimal score;

    /**
     * 用时
     */
    private Integer expendTime;

    /**
     * 提交时间
     */
    private String commitTime;
}
