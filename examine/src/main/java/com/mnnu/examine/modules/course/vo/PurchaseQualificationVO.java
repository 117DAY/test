package com.mnnu.examine.modules.course.vo;

import com.mnnu.examine.common.utils.GwyUtils;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class PurchaseQualificationVO {
    @NotNull
    @Pattern(regexp = GwyUtils.REG_LESSON_ID
            ,message = "请输入正确的课程号")
    private String  id;

    @NotNull
    @Pattern(regexp = GwyUtils.REG_LESSON_TYPE
            ,message = "请输入正确的课程类型")
    private String  type;
}
