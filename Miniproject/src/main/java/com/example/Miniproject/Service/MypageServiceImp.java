package com.example.Miniproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Miniproject.DTO.MypageDTO;
import com.example.Miniproject.Mapper.MypageMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MypageServiceImp implements MypageService {
	
	@Autowired
	private MypageMapper mm;
	
	@Override
	public int insert(MypageDTO m) {
		return mm.insert(m);
	}
	
}
