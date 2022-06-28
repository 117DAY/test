package com.mnnu.examine.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.vo.PhoneWhitCodeVO;
import com.mnnu.examine.modules.sys.vo.RegisterUserVO;

import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     *
     *
     * @description: 更新头像
     * @param headPortrait
     * @return:
     * @author: Geralt
     * @time: 2021/11/26 17:30
     */
    public int updateAvatarByUserId(Map<String, Object> form,Long id);

    /**
     * 查找用户名是否已经存在
     * @param username 用户名
     * @return
     * @author JOJO
     */
    boolean queryUsernameExist(String username);

    /**
     * 查找手机号是否已经注册
     * @param phone
     * @return
     */
    boolean queryPhoneExist(String phone);

    /**
     * 用户注册
     * @param registerUserVO 注册vo
     * @return Boolean
     * @author JOJO
     */
    boolean registerUser(RegisterUserVO registerUserVO);

    /**
     * 更新用户信息
     * @param userEntity 用户信息
     * @param id 用户id
     * @return
     * @author JOJO
     */
    boolean updateUserInfo(UserEntity userEntity,Long id);

    /**
     * 根据id修改用户名
     * @param id
     * @param username
     */
    void updateUsernameById(Long id, String username);

    boolean changePassword(Map<String,Object> form,Long id);



    /**
     * 更新用户的手机
     * @param id
     * @param phone
     * @return
     * @author JOJO
     */
    boolean updateUserPhone(Long id,String phone);

    /**
     * 更新用户名
     * @param id
     * @param phone
     * @return
     */
    boolean updateUsername(Long id,String username);
}

