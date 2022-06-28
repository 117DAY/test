package com.mnnu.examine.modules.teacher.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.teacher.entity.TeacherInfoEntity;
import com.mnnu.examine.modules.teacher.vo.TeacherInfoRegisterVO;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
public interface TeacherInfoService extends IService<TeacherInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存注册信息
     *
     * @param teacherInfo
     */
    void saveRegisterInfo(TeacherInfoRegisterVO teacherInfo);

    /**
     * 更新抽成
     *
     * @param teacherId     老师id
     * @param addCommistion
     */
    void updateCommistion(Integer teacherId, BigDecimal addCommistion);

    /**
     * 获得临时教师
     * @return
     * @author JOJO
     */
    TeacherInfoEntity getTemTeacher();


}

