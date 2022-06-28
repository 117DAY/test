package com.mnnu.examine.modules.teacher.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mnnu.examine.modules.teacher.entity.TeacherInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
@Mapper
public interface TeacherInfoDao extends BaseMapper<TeacherInfoEntity> {

    /**
     * 更新抽成
     *
     * @param teacherId     老师id
     * @param addCommistion 添加commistion
     */
    void updateCommistion(@Param("teacherId") Integer teacherId, @Param("addCommistion") BigDecimal addCommistion);
}
