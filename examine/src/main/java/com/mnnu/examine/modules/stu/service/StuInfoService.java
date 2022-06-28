package com.mnnu.examine.modules.stu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.stu.entity.StuInfoEntity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
public interface StuInfoService extends IService<StuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 更新积分
     *
     * @param stuId 学生id
     * @param addPoint
     */
    void updatePoint(Long stuId, BigDecimal addPoint);
}

