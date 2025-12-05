package com.example.Miniproject.Mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.Miniproject.DTO.MypageDTO;

@Mapper
public interface MypageMapper {
	
	int insert(MypageDTO m);
}
