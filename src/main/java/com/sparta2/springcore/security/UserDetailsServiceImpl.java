package com.sparta2.springcore.security;




import com.sparta2.springcore.model.User;
import com.sparta2.springcore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//역할
// SpringSecurity 가 보내준 username 으로 DB를 탐색해 회원인지 여부 알아보기

@Service                                //UserDetailsService 인터페이스로 class 구현
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                //없는 회원일 경우의 예외 : 유저를 찾을 수 없다.
                .orElseThrow(() -> new UsernameNotFoundException("Can't find " + username));

        //회원 정보가 있을 경우 UserDetails 로 전달
        return new UserDetailsImpl(user);
    }

}