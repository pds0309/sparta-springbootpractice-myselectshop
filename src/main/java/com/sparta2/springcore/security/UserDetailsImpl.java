package com.sparta2.springcore.security;

import com.sparta2.springcore.model.User;
import com.sparta2.springcore.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

//UserDetails 인터페이스를 구현한 클래스
public class UserDetailsImpl implements UserDetails {


    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    //인터페이스에 대한 함수 구현내용

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        return true;
    }

    //인가에 대한 부분
    // 관리자 권한 처리에서..
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.emptyList();
//    }
    private static final String ROLE_PREFIX = "ROLE_";

    //관리자용 페이지만들기
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole userRole = user.getRole();
        System.out.println("인가가 될까요?");
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + userRole.toString());
        //여러개의 권한 설정이 가능
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        System.out.println(authorities);
        return authorities;
    }
}