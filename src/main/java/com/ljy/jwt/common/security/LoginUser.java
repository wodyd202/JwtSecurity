package com.ljy.jwt.common.security;

import com.ljy.jwt.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static java.util.Arrays.asList;


public class LoginUser implements UserDetails {

    private final String userId;
    private final String password;
    private final boolean active;

    public LoginUser(User user) {
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.active = user.isActive();
    }

    public LoginUser(String userId) {
        this.userId = userId;
        this.password = null;
        this.active = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
