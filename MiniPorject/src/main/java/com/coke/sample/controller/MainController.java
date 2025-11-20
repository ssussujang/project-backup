package com.coke.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

	@GetMapping("/")
	public String gohome() {
		return "index";
	}
	
	
}
