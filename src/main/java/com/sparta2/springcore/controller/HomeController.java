package com.sparta2.springcore.controller;


import com.sparta2.springcore.security.UserDetailsImpl;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HomeController {
    //첫 접속시 index.html 로 연결해준다.
    // 이전에 컨트롤러없이도 index로 연결되었다.
    //      -> springboot 가 컨트롤러가 없으면 자동으로 index.html 을 찾아줌
    @GetMapping("/")
                                    //스프링시큐리티가 로그인된 사용자 정보를 넘겨줌
                                    // username을 빼와서 model에 설정 
                                    // 타임리프에 의해서 "username" 이 전달되고
                                    // index.html 에서 사용 됨.
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("username", userDetails.getUsername());
        return "index";
    }

    //관리자용
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String admin(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("admin", true);

        return "index";
    }
}