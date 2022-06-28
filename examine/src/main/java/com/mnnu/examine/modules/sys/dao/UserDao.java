package com.mnnu.examine.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

    /**
     * 更新退款的用户名
     *
     * @param id       id
     * @param username 用户名
     */
    void updateRefundUsername(@Param("id") Long id, @Param("username") String username);
    int updateRefundPhone(@Param("id")Long id,@Param("user_phone") String phone);
}
