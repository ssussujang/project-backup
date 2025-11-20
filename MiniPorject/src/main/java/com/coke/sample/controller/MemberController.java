package com.coke.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coke.sample.dto.Role;
import com.coke.sample.dto.UserDTO;
import com.coke.sample.security.LoginDetails;
import com.coke.sample.service.UserServieImpl;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	private UserServieImpl usi;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// 로그인
	@GetMapping("/memberLogin")
	public String login() {
		return "member/memberLogin";
	}
	
	// 회원가입
	@GetMapping("/memberRegister")
	public String gosignup() {
		return "member/memberRegister";
	}
	
	@PostMapping("/register.reg")
	public String register(@ModelAttribute UserDTO u) {
		u.setT_pw(passwordEncoder.encode(u.getT_pw()));// 주입받은 PasswordEncoder 인스턴스로 암호화
		
		u.setRole(Role.ROLE_USER);
		
		usi.register(u);
		
		return "/index";
	}
	
	// 회원정보수정
	@GetMapping("/memberinfo")
	public String gomemberinfo() {
		return "member/memberinfo";
	}
	
	@PostMapping("/memberinfoUpdate")
	public String memberinfoupdate(UserDTO u) {
		// 1) DB업데이트
		usi.updatemember(u);
		
		// 2) 현상유지
		LoginDetails oldLoginDetails = (LoginDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		// 2-1) 최신 정보 다시 조회
		UserDTO updateUser = usi.findAlluser(u.getT_name());
		
		// 2-2) 기존 비밀번호 유지
		updateUser.setT_pw(oldLoginDetails.getUserdto().getT_pw());
		
		// 3) UserDtails 다시 생성
		LoginDetails newLoginDetails = new LoginDetails(updateUser);
		
		// 4) 인증토큰 갱신
		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newLoginDetails, newLoginDetails.getPassword(), newLoginDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(newAuth);
		return "redirect:/member/memberinfo";
	}
	
	//회원정보삭제(탈퇴)
	@PostMapping("/memberinfodelete")
	public String memberinfodelete(UserDTO u, HttpServletRequest req) {
		Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
		LoginDetails loginUser = (LoginDetails) auth.getPrincipal();
		
		u = loginUser.getUserdto();
		
		usi.deletemember(u);
		
		// SecurityContext 비우기
		SecurityContextHolder.clearContext();
		
		// 세션 무효화
		req.getSession().invalidate();
		
		return "redirect:/";
	}
}
