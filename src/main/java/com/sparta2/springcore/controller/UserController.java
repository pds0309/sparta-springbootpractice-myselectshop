package com.sparta2.springcore.controller;


import com.sparta2.springcore.dto.SignupRequestDto;
import com.sparta2.springcore.model.User;
import com.sparta2.springcore.model.UserRole;
import com.sparta2.springcore.repository.UserRepository;
import com.sparta2.springcore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;


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

    //----------------------test-----------------------------------------------
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/test/create")
    @ResponseBody
    public User test() {
        // 회원 "user1" 객체 추가
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setEmail("user1@sprata.com");
        user1.setRole(UserRole.USER);
        // 회원 "user1" 객체를 영속화
        //컨트롤러 환경에서 영속성 컨텍스트에 저장
        //1차 캐시로 save한 내용이 저장됨.
        // select(findbyusername) 시 DB를 호출하는것이아니라 객체 영속성 컨텍스트에서 가지고 있는 값을 내려줌
        //
        userRepository.save(user1);

        // 모두 같은 객체 고유아이디를 가진다.
         // 회원 "user1" 을 조회
        User foundUser1 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 을 또 조회
        User foundUser2 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 을 또또 조회
        User foundUser3 = userRepository.findByUsername("user1").orElse(null);

        System.out.println("foundUser1:" + foundUser1 + " ,foundUser2: " + foundUser2 + ", foundUser3:" + foundUser3);

        // 테스트 회원 데이터 삭제
        userRepository.delete(user1);
        return user1;
    }

    @GetMapping("/user/test/delete")
    @ResponseBody
    public User deleteUser() {
        // 테스트 회원 "user1" 생성
        createTestUser1();

        // 회원 "user1" 조회
        User foundUser1 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 삭제
        userRepository.deleteById(foundUser1.getId());

        // 회원 "user1" 조회
        User deletedUser1 = userRepository.findByUsername("user1").orElse(null);

        // -------------------
        // 테스트 회원 "user1" 생성
        createTestUser1();

        // 회원 "user1" 또 조회
        User againUser2 = userRepository.findByUsername("user1").orElse(null);
        System.out.println(againUser2);

        // 테스트 회원 데이터 삭제
        userRepository.delete(againUser2);
        return againUser2;
    }
//업데이트 실패
    @GetMapping("/user/test/update/1")
    @ResponseBody
    public User updateUser1() {
        // 테스트 회원 "user1" 생성
        createTestUser1();

        // 회원 "user1" 객체 추가
        // 회원 "user1" 을 조회
        User user1 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 이 존재하면,
        if (user1 != null) {
            // 회원의 email 변경
            user1.setEmail("updateUser1@sparta.com");
            // 회원의 role 변경 (USER -> ADMIN)
            user1.setRole(UserRole.ADMIN);
        }

        // 회원 "user1" 을 또 조회
        User user2 = userRepository.findByUsername("user1").orElse(null);

        System.out.println(user1);
        System.out.println(user2);
        System.out.println(user1.getId());
        System.out.println(user2.getId());
        System.out.println(user1.getEmail());
        System.out.println(user2.getEmail());
        System.out.println(user1.getRole());
        System.out.println(user2.getRole());

        // 테스트 회원 데이터 삭제
        userRepository.delete(user2);

        return user2;
    }
// repo.save 로 DB에 업데이트 반영하기
    @GetMapping("/user/test/update/2")
    @ResponseBody
    public User updateUser2() {
        // 테스트 회원 "user1" 생성
        createTestUser1();

        // 회원 "user1" 객체 추가
        // 회원 "user1" 을 조회
        User user1 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 이 존재하면,
        if (user1 != null) {
            // 회원의 email 변경
            user1.setEmail("updateUser1@sparta.com");
            // 회원의 role 변경 (USER -> ADMIN)
            user1.setRole(UserRole.ADMIN);
        }

        // user1 을 저장
        userRepository.save(user1);

        System.out.println(user1);
        System.out.println(user1.getId());
        System.out.println(user1.getEmail());
        System.out.println(user1.getRole());

        // 테스트 회원 데이터 삭제
        userRepository.delete(user1);

        return user1;
    }
// @transactional 로 DB에 반영하기 - Dirty Check
    @GetMapping("/user/test/update/3")
    @ResponseBody
    @Transactional
    public void updateUse3() {
        // 테스트 회원 "user1" 생성
        createTestUser1();

        // 회원 "user1" 객체 추가
        // 회원 "user1" 을 조회
        User user1 = userRepository.findByUsername("user1").orElse(null);
        // 회원 "user1" 이 존재하면,
        if (user1 != null) {
            // 회원의 email 변경
            user1.setEmail("updateUser1@sparta.com");
            // 회원의 role 변경 (USER -> ADMIN)
            user1.setRole(UserRole.ADMIN);
        }

        System.out.println(user1);
        System.out.println(user1.getId());
        System.out.println(user1.getEmail());
        System.out.println(user1.getRole());

        // TODO: DB 에서 테스트 회원 "user1" 데이터 삭제 꼭 직접 해 주세요~!!
    }

    private void createTestUser1() {
        // 회원 "user1" 객체 추가
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setEmail("user1@sprata.com");
        user1.setRole(UserRole.USER);
        // 회원 "user1" 객체를 영속화
        userRepository.save(user1);
    }




}


