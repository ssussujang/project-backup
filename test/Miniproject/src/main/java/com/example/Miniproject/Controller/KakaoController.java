package com.example.Miniproject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KakaoController {
	
	@Autowired
	
	
	@GetMapping("/login/kakao")
	public String goKakao() {
		return "signup";
	}
	
}
