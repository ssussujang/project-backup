package com.test.ocr.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	private String u_id;
	private String u_pw;
	private String u_name;
	private String u_phone;
	
	
	
}
