package com.example.Miniproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers(
							"/",
							"/login",
							"/signup",
							"/css/**",
							"/js/**",
							"/image/**",
							"/oauth2/**",
							"/login/oauth2/**",
							"/auth/kakao/**"
							).permitAll()
					
							.anyRequest().authenticated()
			)
			
			.formLogin(login -> login
					.loginPage("/login")
					.defaultSuccessUrl("/",true)
					.permitAll()
					)
			
			.oauth2Login(oauth -> oauth
					.loginPage("/login")
					.defaultSuccessUrl("/", true)
			)
			
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
			);
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
