package com.example.Miniproject.Mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.Miniproject.DTO.MemberDTO;

@Mapper
public interface MemberMapper {
	int insert(MemberDTO m);
}
