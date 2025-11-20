package com.coke.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coke.sample.dto.UserDTO;
import com.coke.sample.mapper.UserMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServieImpl  implements UserService{
	
	@Autowired
	private UserMapper um;
	
	
	@Override
	public void register(UserDTO u) {
			System.out.println("DB 등록 시도: " + u);  // 확인용 콘솔 출력
			um.register(u);
			System.out.println("DB 등록 완료");

	}
	
	@Override
	public void updatemember(UserDTO u) {
		um.updatemember(u);
		
	}

	@Override
	public UserDTO findUser(String t_name) {
		return um.findUser(t_name);
	}
	
	@Override
	public UserDTO findAlluser(String t_name) {
		return um.findAllUser(t_name);
	}
	
	@Override
	public void deletemember(UserDTO u) {
		um.deletemember(u);
		System.out.println("계정삭제 완료!");
		
	}
}
