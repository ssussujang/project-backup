package com.coke.sample.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coke.sample.dto.UserDTO;
import com.coke.sample.mapper.UserMapper;


@Service
public class LoginDetailsService implements UserDetailsService  {
	
	private final UserMapper UserMapper;
	private final PasswordEncoder passwordEncoder;
	
	public LoginDetailsService(UserMapper um, PasswordEncoder pwe) {
		this.UserMapper = um;
		this.passwordEncoder = pwe;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		UserDTO userDTO = UserMapper.findUser(username);
		if (userDTO == null) {
			throw new UsernameNotFoundException("사용자 없음");
		}
		System.out.println("loadUserByUsername: " + username);
		
		
		return new LoginDetails(userDTO);
	}
	
	
	
	
}
