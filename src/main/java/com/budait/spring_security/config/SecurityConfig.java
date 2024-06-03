package com.budait.spring_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity   // 메서드 보안 활성화
@EnableWebSecurity     // 웹 보완 활성화 선언
@Configuration      // 스프링 설정 클래스임을 선언
public class SecurityConfig {

    @Autowired // 의존성 주입 (UserDetailsService 타입의 컴포넌트 => UserService)
    private UserDetailsService userDetailsService;


    // AuthenticationManager 인증 관리자
    @Bean   // 스프링 빈으로 등록할 경우 사용자 인증을 처리
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    // 평문 비밀번호를 암호화하여 DB에 저장 (암호화 방법 : BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean       // 스프링 컨테이너가 관리하는 Bean 객체성을 선언하는 애노테이션
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
        // CSRF 보호 비활성화 () (REST API 요청으로 비활성화)
        // CSRF 웹 보안 공격, 사용자가 요청할 때 인지 못한 상태에서 원하지 않은 액션을 수행하게 하는 공격
        // CSRF 보호가 되어있는 웹에 접근하기 위해서는 CSRF 토큰이 필요하고 검증
                .authorizeRequests(auth -> auth
                        .requestMatchers("/test", "/register", "/auth").permitAll()   // test, register 라는 요청에 대해서는 모두 허용
                        .anyRequest().authenticated()   // 모든 요청에 대해서 인증을 요구
                )
                .formLogin(form -> form
                        .loginPage("/login")        // 로그인 페이지 경로
                        .permitAll()        // 로그인 페이지는 모두에게 허용
                )
                .logout(login -> login
                .permitAll());      // 로그아웃도 모두에게 허용

        return http.build();
    }
}
