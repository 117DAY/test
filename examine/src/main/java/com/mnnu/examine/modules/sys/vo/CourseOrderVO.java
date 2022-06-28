package com.mnnu.examine.modules.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 用于生成订单的课程重要信息
 *
 * @author qiaoh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseOrderVO {
    /**
     * 课程id
     */
    @NotNull(message = "课程id不能为空")
    @Min(value = 1, message = "课程id不合法")
    Long courseId;

    /**
     * 课程类型【0录播课，1直播课】
     * 课程类型
     */
    @Min(0)
    @Max(1)
    @NotNull(message = "课程类型不能为空")
    Integer courseType;

}
