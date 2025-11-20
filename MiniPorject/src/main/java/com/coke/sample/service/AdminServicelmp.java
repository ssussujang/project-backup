package com.coke.sample.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coke.sample.dto.UserDTO;
import com.coke.sample.mapper.AdminMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminServicelmp implements AdminService{
	
	@Autowired
	private AdminMapper adm;
	
	@Override
	public List<UserDTO> showmember() {
		
		return adm.showmember();
	}
	
	@Override
	public void updateMemberInfo(UserDTO u) {
		adm.updateMemberInfo(u);
	}
	
	@Override
	public void deleteMemberInfo(UserDTO u) {
		adm.deleteMemberInfo(u);
	}
	
	
}
