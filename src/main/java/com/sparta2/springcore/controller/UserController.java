package com.sparta2.springcore.controller;


import com.sparta2.springcore.dto.SignupRequestDto;
import com.sparta2.springcore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


//유저 로그인 관련 요청 controller
@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService service){
        this.userService = service;
    }

    // 회원 로그인 페이지
    @GetMapping("/user/login")
    public String login() {
        //user/login  요청이 들어올 때
        // thymeleaf 에 의해 login String return 시
        // 루트경로로 지정했던 static/ 의  login.html 을 찾음
        return "login";
    }

    // 로그인 에러 페이지
    @GetMapping("/user/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    //signup.html 찾음
    public String signup() {
        return "signup";
    }

    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public String registerUser(SignupRequestDto requestDto){
        userService.registerUser(requestDto);
        return "redirect:/";
    }

    //인가안된 이용자용 에러 페이지
    @GetMapping("/user/forbidden")
    public String forbidden() {
        return "forbidden";
    }

//카카오로그인 요청
    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(String code) {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        userService.kakaoLogin(code);

        return "redirect:/";
    }
}


