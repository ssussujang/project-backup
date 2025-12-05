package com.example.Miniproject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Miniproject.DTO.MemberDTO;
import com.example.Miniproject.Service.MemberserviceImp;

@Controller
public class MemberController {
	
	@Autowired
	private MemberserviceImp ms;
	
	@GetMapping("/signup")
	public String signupForm() {
		return "/member/signup";
	}
	
	@PostMapping("/signup")
	public String signupSubmit(MemberDTO m) {
		ms.insert(m);
		return "redirect:/signup";
	}
	
	@GetMapping("/login")
	public String loginForm() {
		return "/member/login";
	}
	
	@PostMapping("/login")
	public String loginSubmit(MemberDTO m) {
		return "/member/login";
	}
	
	

}
