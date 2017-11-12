package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
/**
 * 测试
 * @title
 * @description
 * @author ljh 
 * @version 1.0
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.service.TestService;
@Controller
public class TestController {
	
	@Autowired
	private TestService testservice;
	
	@RequestMapping("/queryNow")
	@ResponseBody
	public String getNow(){
		
		//1.引入服务
		//2.注入服务
		//3.调用 返回
		
		return testservice.getNow();
	}
}
