package com.example.Miniproject.Mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.Miniproject.DTO.BoardDTO;

@Mapper
public interface BoardMapper {
	
	int insert(BoardDTO b);
}
