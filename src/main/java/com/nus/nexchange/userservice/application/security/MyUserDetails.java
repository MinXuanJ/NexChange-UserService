package com.nus.nexchange.userservice.application.security;
import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {
    private final UserIdentity user;

    public MyUserDetails(UserIdentity user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUserEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 可根据需要实现
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 可根据需要实现
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 可根据需要实现
    }

    @Override
    public boolean isEnabled() {
        return true; // 可根据需要实现
    }
}
