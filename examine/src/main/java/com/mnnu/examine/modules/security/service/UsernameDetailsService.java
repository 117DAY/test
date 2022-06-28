package com.mnnu.examine.modules.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mnnu.examine.modules.security.entity.GwyUser;
import com.mnnu.examine.modules.sys.entity.RoleEntity;
import com.mnnu.examine.modules.sys.entity.RoleUserRelationEntity;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.RoleService;
import com.mnnu.examine.modules.sys.service.RoleUserRelationService;
import com.mnnu.examine.modules.sys.service.UserService;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 用户名密码登录的校验
 *
 * @author qiaoh
 */
@Component
public class UsernameDetailsService implements UserDetailsService {

    @Resource
    UserService userService;

    @Resource
    RoleService roleService;

    @Resource
    RoleUserRelationService relationService;

    /**
     * 根据用户名查出用户信息，以及用户角色，封装到 userDetails 中
     *
     * @param s
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", s);
        UserEntity userEntity = userService.getOne(wrapper);
        if (userEntity == null) {
            throw new InternalAuthenticationServiceException("用户不存在");
        }
        if (userEntity.getShowStatus() == 0) {
            throw new DisabledException("账号不可用");
        }

        // 查出用户对应的角色
        QueryWrapper<RoleUserRelationEntity> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("user_id", userEntity.getId());
        List<RoleUserRelationEntity> list = relationService.list(wrapper1);
        List<Integer> roleIds = list.stream().map(RoleUserRelationEntity::getRoleId).collect(Collectors.toList());

        // 角色名
        QueryWrapper<RoleEntity> wrapper2 = new QueryWrapper<>();
        wrapper2.in("id", roleIds);
        List<RoleEntity> roleEntities = roleService.list(wrapper2);

        CopyOnWriteArrayList<GrantedAuthority> auth = new CopyOnWriteArrayList<>();
        roleEntities.forEach(r -> {
            auth.add(new SimpleGrantedAuthority("ROLE_" + r.getRole()));
        });
        GwyUser gwyUser = new GwyUser(userEntity, userEntity.getUsername(), userEntity.getPassword(), auth);

        return gwyUser;
    }
}
