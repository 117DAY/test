package io.renren.modules.stu.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.stu.entity.StuInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
@Mapper
public interface StuInfoDao extends BaseMapper<StuInfoEntity> {

    /**
     * 更新积分
     *
     * @param stuId    学生id
     * @param addPoint 添加的积分
     */
    void updatePoint(@Param("stuId") Long stuId,@Param("addPoint") BigDecimal addPoint);

    /**
     * 减点
     *
     * @param userId   用户id
     * @param subtract 减去
     */
    void subtractPoint(@Param("userId") Long userId,@Param("subtract") BigDecimal subtract);
}
