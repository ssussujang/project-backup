package com.example.Miniproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Miniproject.DTO.BoardDTO;
import com.example.Miniproject.Mapper.BoardMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardServiceImp implements BoardService{

	@Autowired
	private BoardMapper bm;
	
	@Override
	public int insert(BoardDTO b) {
		return bm.insert(b);
	}
}
