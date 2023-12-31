package com.ll.townforest.base.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // 환경설정
@EnableWebSecurity // 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만듬
@EnableMethodSecurity(prePostEnabled = true)  // PreAuthorize 사용하기 위해 반드시 필요
public class SecurityConfig {
	@Bean
		// 리턴값은 Bean에 등록
		// SecurityFilterChain 빈을 생성하여 스프링 시큐리티 세부 설정 가능
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.formLogin(
				formLogin -> formLogin
					.loginPage("/account/login")
					.defaultSuccessUrl("/")
			)
			.logout(
				logout -> logout
					.logoutUrl("/account/logout")
					.logoutSuccessUrl("/account/login")
			);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
		Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}