package com.coke.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	
	private String t_name;
	private int t_age;
	private Gender t_gender;
	private String t_email;
	private String t_pw;
	private Role role;
	
	public enum Gender {
		남자,여자
	}
	
	
}
