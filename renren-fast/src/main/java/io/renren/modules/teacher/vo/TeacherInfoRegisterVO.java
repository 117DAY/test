package io.renren.modules.teacher.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 老师信息登记
 *
 * @author qiaoh
 * @date 2021/12/01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherInfoRegisterVO {

    /**
     * id
     */
    Long teacherId;
    /**
     * 个人照片
     */
    String photos;
    /**
     * 毕业院校
     */
    @NotNull(message = "毕业院校不能为空")
    String graduateSchool;
    /**
     * 教育背景，学历
     */
    @NotNull(message = "学历背景不能为空")
    String eduBg;
    /**
     * 真实姓名
     */
    private String realName;
}
