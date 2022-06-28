package com.mnnu.examine.modules.sys.controller;


import com.mnnu.examine.common.utils.GwyUtils;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.sys.entity.RoleUserRelationEntity;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.RoleUserRelationService;
import com.mnnu.examine.modules.sys.service.SmsService;
import com.mnnu.examine.modules.sys.service.UserService;
import com.mnnu.examine.modules.sys.vo.PhoneWhitCodeVO;
import com.mnnu.examine.modules.sys.vo.RegisterUserVO;
import com.mnnu.examine.modules.sys.vo.UserWithRoleTypeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
@RestController
@RequestMapping("sys/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Resource
    SmsService smsService;
    @Resource
    RoleUserRelationService roleUserRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        UserEntity user = userService.getById(id);

        return R.ok().put("user", user);
    }

    /**
     * 获取用户vo
     * @author JOJO
     * @return
     */
    @RequestMapping("/userinfo")
    public R userInfo() {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        UserEntity userEntity = userService.getById(id);
//        UserEntity userEntity = userService.getBaseMapper().selectById(id);
        UserWithRoleTypeVO userWithRoleTypeVO = new UserWithRoleTypeVO();
        BeanUtils.copyProperties(userEntity, userWithRoleTypeVO);
        RoleUserRelationEntity roleTypeByUserId = roleUserRelationService.getRoleTypeByUserId(id);
        userWithRoleTypeVO.setRoleType(roleTypeByUserId.getRoleId());
        return R.ok().put("user", userWithRoleTypeVO);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserEntity user) {
        userService.save(user);

        return R.ok();
    }

    /**
     * 更新用户信息
     *
     * @param userEntity 用户信息
     * @return 返回更新好的用户信息
     * @author JOJO
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserEntity userEntity) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        boolean b = userService.updateUserInfo(userEntity, id);
        UserEntity userEntity1 = userService.getBaseMapper().selectById(id);
        UserWithRoleTypeVO userWithRoleTypeVO = new UserWithRoleTypeVO();
        BeanUtils.copyProperties(userEntity1, userWithRoleTypeVO);
        RoleUserRelationEntity roleTypeByUserId = roleUserRelationService.getRoleTypeByUserId(id);
        userWithRoleTypeVO.setRoleType(roleTypeByUserId.getRoleId());
        return R.ok().put("user", userWithRoleTypeVO);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * @param
     * @description: 更新头像
     * @return:
     * @author: Geralt
     * @time: 2021/11/26 17:30
     */
    @RequestMapping("/updateAvatar")
    public R updateAvatarByUserId(@RequestBody Map<String, Object> form) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        int res = userService.updateAvatarByUserId(form, id);
        return res == 1 ? R.ok() : R.error();
    }

    /**
     * 注销当前账户
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public R deleteAccount(@PathVariable("id") Long id) {
        id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        userService.removeById(id);
        return R.ok();
    }

    /**
     * 更改用户名
     *
     * @return {@link R}
     */
    @RequestMapping("/change/username/{id}")
    public R changeUsername(@PathVariable("id") Long id,
                            @RequestParam("username") String username) {
        id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        userService.updateUsernameById(id, Objects.requireNonNull(username, "用户名不可为空"));

        return R.ok();
    }

    /**
     * 查找用用户名是否已经存在
     *
     * @param username 用户名
     * @return
     * @author JOJO
     */
    @RequestMapping("/exist")
    public R queryUsernameExist(@RequestParam("username") String username) {
        boolean b = userService.queryUsernameExist(username);
        return R.ok().put("useable", b);
    }

    /**
     * 查询是否手机被注册
     *
     * @param phone 手机号
     * @return
     * @author JOJO
     */
    @RequestMapping("/phone")
    public R queryPhoneExist(@RequestParam("phone") String phone) {
        boolean b = userService.queryPhoneExist(phone);
        return R.ok().put("phone", b);
    }

    /**
     * 注册用户
     *
     * @param registerUserVO 注册对象
     * @return Boolean
     * @author JOJO
     */
    @RequestMapping("/register")
    public R registerUser(@Validated @RequestBody RegisterUserVO registerUserVO) {
        boolean b = userService.registerUser(registerUserVO);
        return R.ok().put("register", b);
    }

    /**
     * 获取手机验证码
     *
     * @return
     */
    @RequestMapping("/getphonecode")
    public R getPhoneCode(@RequestBody Map<String, String> phoneMap) {
        String phone = phoneMap.get("phone");

        boolean phoneCode = smsService.getPhoneCode(phone);
        if (phoneCode) {
            return R.ok().put("get", true);
        }
        return R.ok().put("get", false);
    }

    /**
     * 检查手机验证码
     * 先校验后判断
     *
     * @return
     * @author JOJO
     */
    @RequestMapping("/checkphonecode")
    public R checkPhoneCode(@RequestBody Map<String, String> phoneWithCode) {
        System.out.println(phoneWithCode);
        String phone = phoneWithCode.get("phone");
        String code = phoneWithCode.get("code");
        boolean right = smsService.queryPhoneWithPhone(phone, code);
        if (right) {
            return R.ok().put("right", true);
        }
        return R.ok().put("right", false);
    }

    /**
     * 更换手机
     * 先判断手机验证码是否正确后更新
     *
     * @return
     * @author JOJO
     */
    @RequestMapping("changephone")
    public R changePhone(@Validated @RequestBody PhoneWhitCodeVO phoneWhitCodeVO) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        boolean b = smsService.checkPhoneWithCode(phoneWhitCodeVO);
        if (b) {
            boolean b1 = userService.updateUserPhone(id, phoneWhitCodeVO.getNewPhone());
            if (b1) {
                return R.ok().put("done", true);
            }
        }
        return R.ok().put("done", false);
    }

    /**
     * 更改密码
     *
     * @param form 形式
     * @return {@link R}
     */
    @RequestMapping("/cp")
    public R changePassword(@RequestBody Map<String, Object> form) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        boolean flag = userService.changePassword(form, id);
        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 修改用户名
     *
     * @param form
     * @return
     * @author JOJO
     */
    @RequestMapping("/changeusername")
    public R changeUsername(@RequestBody Map<String, String> form) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        String username = form.get("username");
        boolean b = GwyUtils.validateUsername(username);
        if (b) {
            boolean flag = userService.updateUsername(id, username);
            return flag ? R.ok() : R.error();
        } else {
            return R.error();
        }
    }

}
