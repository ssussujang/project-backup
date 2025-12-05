package com.example.Miniproject.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class MypageDTO {
	
	private long board_no;
	private String board_writer;
	private String board_wpahr;
	private String board_content;
	private Date board_time;
}
