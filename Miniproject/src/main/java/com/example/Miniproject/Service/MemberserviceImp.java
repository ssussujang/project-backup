package com.example.Miniproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Miniproject.DTO.MemberDTO;
import com.example.Miniproject.Mapper.MemberMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MemberserviceImp implements MemberService {
	
	@Autowired
	private MemberMapper mm;
	
	@Override
	public int insert(MemberDTO m) {
		return mm.insert(m);
	}
}
