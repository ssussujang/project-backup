package com.coke.sample.service;


import com.coke.sample.dto.UserDTO;


public interface UserService {
	void register(UserDTO u);
	UserDTO findUser (String t_name);
	UserDTO findAlluser(String t_name);
	void updatemember(UserDTO u);
	void deletemember(UserDTO u);
}
