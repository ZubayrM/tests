package com.zubayr.tests.security;

import com.zubayr.tests.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class UserDetailImpl implements UserDetails {

    private String username;
    private String password;
    private Set<SimpleGrantedAuthority> roles;
    private boolean isBlocked;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isBlocked;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isBlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isBlocked;
    }

    @Override
    public boolean isEnabled() {
        return !isBlocked;
    }

    public static UserDetailImpl getInstance(User user) {
        return new UserDetailImpl(
                user.getUserName(),
                user.getPassword(),
                Set.of(new SimpleGrantedAuthority(user.getRole().name())),
                user.getIsBlocked()
        );
    }
}
