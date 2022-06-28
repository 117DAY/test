package io.renren.modules.teacher.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
@Data
@TableName("gwy_teacher_info")
public class TeacherInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 教师的id
     */
    private Long teacherId;
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

}
