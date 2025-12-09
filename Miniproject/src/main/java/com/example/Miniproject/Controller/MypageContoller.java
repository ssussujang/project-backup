package com.example.Miniproject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Miniproject.DTO.MypageDTO;
import com.example.Miniproject.Service.MypageServiceImp;

@Controller
public class MypageContoller {

	@Autowired
	private MypageServiceImp ms;
	
	@GetMapping("/mypage")
	public String goMypage() {
		return "mypage/mypage";
	}
	
	@PostMapping("/mypage")
	public String mypage(MypageDTO m) {
		ms.insert(m);
		return "mypage/mypage";
	}
}
