package com.sparta2.springcore.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
//관리자페이지위한 추가
// 컨트롤러에서 인가가 필요한 API 에 API마다 접근가능한 ROLE을 설정할 수 있다. @Secured
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    //BCrypt 암호화
    @Bean // bean - 다른곳에서 사용할 수 있게 하기 위함
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests()

                //.anyRequest().authenticated()
                //-> 모든 요청에 대해 인증을 하겠다
                // -> css 파일 요청에 대한 인증도 필요하다.
                // css 파일은 로그인 요청을 하기 전에도 나와야 한다.
                // 따라서 css파일에 대해서는 인증 전에 허용되도록 해줘야함
                    // ------------------- 추가
                //images 폴더를 login 인증 전에 허용
                    // Basically the Spring supports "Ant style globbing".
                    // Thus path="/*" matches any URL in the "/" directory1,
                    // and path="/**" matches any URL in the entire directory tree.
                    .antMatchers("/images/**").permitAll()
                // css 폴더를 login 인증 전에 허용
                    .antMatchers("/css/**").permitAll()
                    // -----------------------------
                    .antMatchers("/user/**").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                //어떤 요청이 오든 로그인 과정이 없으면
                .anyRequest()
                //로그인을 하겠다
                .authenticated()
                .and()
                //로그인페이지에 대해서는
                .formLogin()
//-----------------로그인,에러페이지 지정 추가 ------------------------------------------------
                .loginPage("/user/login")
                .failureUrl("/user/login/error")
//---------------------------------------------------------------------

                    // 로그인 완료시 이동할 위치
                .defaultSuccessUrl("/")
                //허용
                .permitAll()
                .and()
                // 로그아웃 기능
                .logout()
                .logoutUrl("/user/logout")
                //허용
                .permitAll()

                //인가되지않을 때 페이지 이동 처리
                .and()
                .exceptionHandling()
                .accessDeniedPage("/user/forbidden");
    }

}

