package com.coke.sample.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coke.sample.dto.UserDTO;
import com.coke.sample.service.AdminServicelmp;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminServicelmp asl;
	
	@GetMapping("/memberinfo")
	public String showmemeber(Model model) {
		List<UserDTO> members = asl.showmember();
		model.addAttribute("members",members);
		return "admin/memberinfo";
	}
	
	@PostMapping("/updateMemberInfo")
	public String updateMemberInfo(UserDTO u, @RequestParam("action") String action) {
		
		System.out.println("==== updateMemberInfo ====");
	    System.out.println("action = " + action);
	    System.out.println("t_name   = " + u.getT_name());
	    System.out.println("t_gender = " + u.getT_gender());
	    System.out.println("t_email  = " + u.getT_email());
	    System.out.println("t_pw     = " + u.getT_pw());
		
		
		if ("update".equals(action)) {
			asl.updateMemberInfo(u);
		}else if ("delete".equals(action)) {
			asl.deleteMemberInfo(u);
		}
		return "redirect:/admin/memberinfo";
	}
	
}
