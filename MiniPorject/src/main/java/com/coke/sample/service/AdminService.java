package com.coke.sample.service;

import java.util.List;

import com.coke.sample.dto.UserDTO;

public interface AdminService {
	List<UserDTO> showmember();
	void updateMemberInfo(UserDTO u);
	void deleteMemberInfo(UserDTO u);
}
