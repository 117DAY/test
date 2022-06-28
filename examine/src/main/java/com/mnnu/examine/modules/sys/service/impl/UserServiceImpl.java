package com.mnnu.examine.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.exception.GwyException;
import com.mnnu.examine.common.utils.*;
import com.mnnu.examine.modules.course.entity.CommentsEntity;
import com.mnnu.examine.modules.course.service.CommentsService;
import com.mnnu.examine.modules.stu.entity.StuInfoEntity;
import com.mnnu.examine.modules.stu.service.StuInfoService;
import com.mnnu.examine.modules.sys.dao.UserDao;
import com.mnnu.examine.modules.sys.entity.RefundEntity;
import com.mnnu.examine.modules.sys.entity.RoleUserRelationEntity;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.RefundService;
import com.mnnu.examine.modules.sys.service.RoleUserRelationService;
import com.mnnu.examine.modules.sys.service.UserService;
import com.mnnu.examine.modules.sys.vo.PhoneWhitCodeVO;
import com.mnnu.examine.modules.sys.vo.RegisterUserVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Pattern;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
    @Resource
    RedisUtils redisUtils;

    @Resource
    RoleUserRelationService roleUserRelationService;

    @Resource
    StuInfoService stuInfoService;
    @Resource
    RefundService refundService;
    @Resource
    CommentsService commentsService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @description: 更新头像
     * @para headPortrait
     * @return:
     * @author: Geralt
     * @time: 2021/11/26 17:30
     */

    @Override
    public int updateAvatarByUserId(Map<String, Object> form, Long userId) {
        UpdateWrapper<UserEntity> user = new UpdateWrapper<>();
        String headPortrait = (String) form.get("headPortrait");
        String updateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        user.eq("id", userId)
                .set("head_portrait", headPortrait)
                .set("update_time", updateTime);
        return getBaseMapper().update(null, user);

    }

    /**
     * 查找用户名是否存在
     *
     * @param username 用户名
     * @return Boolean
     * @author JOJO
     */

    @Override
    public boolean queryUsernameExist(String username) {
        return (getBaseMapper().selectCount(new QueryWrapper<UserEntity>().eq("username", username))) == 0;
    }

    /**
     * 查找手机是否已经被注册
     *
     * @param phone 手机号
     * @return Boolean
     * @author JOJO
     * 存在返回flase
     * 不存在返回true
     */
    @Override
    public boolean queryPhoneExist(String phone) {
        return (getBaseMapper().selectCount(new QueryWrapper<UserEntity>().eq("phone", phone))) == 0;
    }

    /**
     * 用户注册并在关系表插入学生关系
     *
     * @param registerUserVO 注册VO
     * @return
     * @author JOJO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerUser(RegisterUserVO registerUserVO) {
        UserEntity userEntity = new UserEntity();
        String username = registerUserVO.getUsername();
        Integer count = getBaseMapper().selectCount(new QueryWrapper<UserEntity>().eq("username", username));
        if (count == 1) {
            throw new GwyException(GwyConstant.BizCode.USERNAME_EXIST.getCode(), GwyConstant.BizCode.USERNAME_EXIST.getMessage());
        } else {
            //从redis获取默认头像
            String defaultHeadPicUrl = redisUtils.get("defaultHeadPicUrl");
            userEntity.setHeadPortrait(defaultHeadPicUrl);
            userEntity.setUsername(username);
            userEntity.setPhone(registerUserVO.getPhone());
            String localTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            userEntity.setCreateTime(localTime);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encode = bCryptPasswordEncoder.encode(registerUserVO.getPassword());
            userEntity.setPassword(encode);
            getBaseMapper().insert(userEntity);
            // 插入角色信息，默认学生角色
            RoleUserRelationEntity entity = new RoleUserRelationEntity();
            entity.setRoleId(GwyConstant.RoleConstant.STUDENT.getId());
            entity.setUserId(userEntity.getId());
            roleUserRelationService.save(entity);
            // 插入学生信息
            StuInfoEntity stuInfoEntity = new StuInfoEntity();
            stuInfoEntity.setStuId(userEntity.getId());
            stuInfoEntity.setPoint(new BigDecimal("0.00"));
            stuInfoService.save(stuInfoEntity);
            return true;
        }
    }

    /**
     * 更新用户信息
     *
     * @param userEntity 用户信息
     * @param id         用户id
     * @return
     * @author JOJO
     */
    @Override
    public boolean updateUserInfo(UserEntity userEntity, Long id) {
        userEntity.setId(id);
        int i = getBaseMapper().updateById(userEntity);
        return i == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUsernameById(Long id, String username) {
        if (!this.queryUsernameExist(username)) {
            throw new GwyException(GwyConstant.BizCode.USERNAME_EXIST.getMessage(), GwyConstant.BizCode.USERNAME_EXIST.getMessage(), GwyConstant.BizCode.USERNAME_EXIST.getCode());
        }
        // 修改user表中用户名
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setUsername(username);
        userEntity.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.updateById(userEntity);
        // 修改其他引用了用户名的表
        getBaseMapper().updateRefundUsername(id, username);
    }

    @Override
    public boolean changePassword(Map<String, Object> form, Long id) {
        String newPs = (String) form.get("newPs");
        boolean b = GwyUtils.validatePassword(newPs);
        if (b) {
            UserEntity userEntity = getBaseMapper().selectById(id);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encode = bCryptPasswordEncoder.encode(newPs);
            userEntity.setPassword(encode);
            getBaseMapper().updateById(userEntity);
            return true;
        }
        return false;

    }


    /**
     * 更新用户的手机
     *
     * @param id
     * @param phone
     * @return
     * @author JOJO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserPhone(Long id, String phone) {
        //更新user表的phone
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setPhone(phone);
        int updateUser = getBaseMapper().updateById(userEntity);
        //更新record
        int updateRefundPhone = getBaseMapper().updateRefundPhone(id, phone);
        RefundEntity refundEntity = new RefundEntity();
        refundEntity.setUserId(id);
        refundEntity.setUserPhone(phone);
        int b = refundService.getBaseMapper().update(refundEntity,new QueryWrapper<RefundEntity>().eq("user_id",id));
        return updateUser == 1 ;


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUsername(Long id, String username) {
        if (GwyUtils.validateUsername(username)){
            UserEntity userEntity = new UserEntity();
            //更新用户表的用户名
            userEntity.setId(id);
            userEntity.setUsername(username);
            int updateById = getBaseMapper().updateById(userEntity);
            //更新退款表的用户名
            RefundEntity refundEntity = new RefundEntity();
            refundEntity.setUserId(id);
            refundEntity.setUsername(username);
            int user_id = refundService.getBaseMapper().update(refundEntity, new QueryWrapper<RefundEntity>().eq("user_id", id));
            // 更新评论表的用户名
            // 更新评论表中的评论者的用户名
            CommentsEntity commentsEntity1 = new CommentsEntity();
            commentsEntity1.setCommentName(username);
            int comment_id = commentsService.getBaseMapper().update(commentsEntity1, new QueryWrapper<CommentsEntity>().eq("comment_id", id));

            //更新评论表中的被评论者的用户名
            CommentsEntity commentsEntity2 = new CommentsEntity();
            commentsEntity2.setReplyName(username);
            int reply_name = commentsService.getBaseMapper().update(commentsEntity2, new QueryWrapper<CommentsEntity>().eq("reply_id", id));
            return updateById != 0 ;
        }
     return false;
    }


}
