package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/page/{page}")
	public String showPage(@PathVariable String page,String url,Model model){
		System.out.println("url>>>>>>"+url);
		
		model.addAttribute("url", url);
		return page;
	}
}
