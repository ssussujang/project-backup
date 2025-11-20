package com.coke.sample.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.coke.sample.dto.UserDTO;

@Mapper
public interface UserMapper {
	void register(UserDTO u);
	
	UserDTO findUser (String t_name);
	
	UserDTO findAllUser(String t_name);
	
	void updatemember(UserDTO u);
	
	void deletemember(UserDTO u);
	
}
