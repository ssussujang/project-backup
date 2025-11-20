package com.coke.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.coke.sample.dto.UserDTO;

@Mapper
public interface AdminMapper {
	List<UserDTO> showmember();
	void updateMemberInfo(UserDTO u);
	void deleteMemberInfo(UserDTO u);
}
