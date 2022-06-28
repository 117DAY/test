package com.mnnu.examine.modules.sys.vo;

import lombok.Data;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author qiaoh
 */
@Data
public class UserRespVO {
    private Long id;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 用户名
     */
    private String username;
    /**
     * 性别【0男性，1女性】
     */
    private Integer gender;
    /**
     * 出生年份，用来计算年龄
     */
    private String birthYear;
    /**
     * 头像
     */
    private String headPortrait;
    /**
     * 个人简介
     */
    private String personalIntroduction;
    /**
     * 个人签名，类似qq个签
     */
    private String motto;
    /*
    * 用户角色.(1,学生,2教师)
    * */
    private Integer roleType;
}
