package com.mnnu.examine.modules.security.entity;

import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.request.async.WebAsyncManager;

import java.util.Collection;

/**
 * @author qiaoh
 */
public class GwyUser extends User {

    private UserEntity userEntity;

    public GwyUser(UserEntity userEntity, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userEntity = userEntity;
    }

    public GwyUser(UserEntity userEntity, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
