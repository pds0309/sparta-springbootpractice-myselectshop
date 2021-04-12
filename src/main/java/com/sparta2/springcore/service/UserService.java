package com.sparta2.springcore.service;


import com.sparta2.springcore.security.KakaoOAuth2;
import com.sparta2.springcore.security.KakaoUserInfo;
import com.sparta2.springcore.dto.SignupRequestDto;
import com.sparta2.springcore.model.User;

import com.sparta2.springcore.model.UserRole;
import com.sparta2.springcore.repository.UserRepository;
import com.sparta2.springcore.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//회원가입에대한 비즈니스 로직

@Service
public class UserService {
    private final KakaoOAuth2 kakaoOAuth2;
    private final AuthenticationManager authenticationManager;
    //bean 으로 등록했던 Bcrypt 추가
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired                                       //bean으로 등록했던거 DI주입받음
    public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder,KakaoOAuth2 kakaoOAuth2, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kakaoOAuth2 = kakaoOAuth2;
        this.authenticationManager = authenticationManager;
    }

    // 카카오 X
//    public void registerUser(SignupRequestDto requestDto) {
//
//        String username = requestDto.getUsername();
//        //비밀번호 인코딩 - 암호화된 패스워드가 나옴
//        String password = passwordEncoder.encode(requestDto.getPassword());
//        // 회원 ID 중복 확인
//        Optional<User> found = userRepository.findByUsername(username);
//        if (found.isPresent()) {
//            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
//        }
//
//        String email = requestDto.getEmail();
//        // 사용자 ROLE 확인
//        UserRole role = UserRole.USER;
//        if (requestDto.isAdmin()) {
//            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
//                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
//            }
//            role = UserRole.ADMIN;
//        }
//
//        User user = new User(username, password, email, role);
//        userRepository.save(user);
//    }
    public User registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String email = requestDto.getEmail();
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }
        found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 이메일이 존재합니다.");
        }
        // 패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());
//        String email = requestDto.getEmail();
        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRole.ADMIN;
        }

        User user = new User(username, password, email, role);
        userRepository.save(user);
        return user;
    }

    public void kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        // 우리 DB 에서 회원 Id 와 패스워드
        // 회원 Id = 카카오 nickname
        String username = nickname;
        // 패스워드 = 카카오 Id + ADMIN TOKEN
        String password = kakaoId + ADMIN_TOKEN;

        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        // 카카오 정보로 회원가입
        if (kakaoUser == null) {
            User existUser = userRepository.findByEmail(email).orElse(null);
            if(existUser != null){
                kakaoUser = existUser;
                kakaoUser.addKakaoId(kakaoId);
                userRepository.save(kakaoUser);
            }
            else{
                // 패스워드 인코딩
                String encodedPassword = passwordEncoder.encode(password);
                // ROLE = 사용자
                UserRole role = UserRole.USER;

                kakaoUser = new User(nickname, encodedPassword, email, role, kakaoId);
                userRepository.save(kakaoUser);
            }
        }

        // 로그인 처리

        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        //System.out.println(kakaoUser);
        //System.out.println(userDetails);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null , userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

//    // 스프링 시큐리티 통해  인증된 사용자로 등록
//    UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
//    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//SecurityContextHolder.getContext().setAuthentication(authentication);
}


