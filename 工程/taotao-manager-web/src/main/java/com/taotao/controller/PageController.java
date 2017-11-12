package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	//url模板映射的知识  占位符
	@RequestMapping("/{page}")
	public String showItemList(@PathVariable String page){
		return page;
	}
	
}
