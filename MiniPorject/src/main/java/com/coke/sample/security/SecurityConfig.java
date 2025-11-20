package com.coke.sample.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public AuthenticationProvider authenticataionProvider(
			LoginDetailsService loginDetailsService,
			PasswordEncoder passwordEncoder
			){
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(loginDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, RedirectLoggingFilter redirctLogginFilter, AuthenticationProvider authenticationProvider) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/","/index","/member/memberLogin","/member/memberRegister","/member/register.reg","/error").permitAll()
					.requestMatchers("/admin/**").hasRole("ADMIN")
					.anyRequest().authenticated()
			)
			.formLogin(form -> form
					.loginPage("/member/memberLogin")
					.loginProcessingUrl("/login")
					.usernameParameter("t_name")
					.passwordParameter("t_pw")
					.defaultSuccessUrl("/")
					.permitAll()
			)
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
			)
			.csrf(csrf -> csrf.disable());
		http.authenticationProvider(authenticationProvider);
		
		http.addFilterBefore(redirctLogginFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
		
			
		return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/css/**","/js/**","/img/**","/favicon.ico");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
}