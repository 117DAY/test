package io.renren.modules.teacher.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 教师的全部信息视图对象
 *
 * @author qiaoh
 */
@Data
public class TeacherTotalInfoVO {

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
     * 微信的id
     */
    private String openid;
    /**
     * 出生年份
     */
    private String birthYear;
    /**
     * 注册时间
     */
    private String createTime;
    /**
     * 修改个人信息时间
     */
    private String updateTime;
    /**
     * 头像
     */
    private String headPortrait;
    /**
     * 最后一次登录时间
     */
    private String lastLoginTime;
    /**
     * 最后一次登录ip
     */
    private String lastLoginIp;
    /**
     * 个人简介
     */
    private String personalIntroduction;
    /**
     * 个人签名，类似qq个签
     */
    private String motto;
    /**
     * 个人图片的地址
     */
    private String photos;
    /**
     * 毕业院校
     */
    private String graduateSchool;
    /**
     * 学历
     */
    private String eduBg;
    /**
     * 抽成余额
     */
    private BigDecimal commistion;

    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 教师的角色
     */
    private Integer roleId;
}
