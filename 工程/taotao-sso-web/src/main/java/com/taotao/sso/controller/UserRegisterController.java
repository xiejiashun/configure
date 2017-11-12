package com.taotao.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.UserSessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserRegisterService;

@Controller
public class UserRegisterController {
	
	@Autowired
	private UserRegisterService registerservice;
	
	//校验数据是否可以用
	
	//url:/user/check/{param}/{type}
	//method：get
	//参数:param  type
	//返回值:json taotaoresult
	
	@RequestMapping(value="/user/check/{param}/{type}",method=RequestMethod.GET)
	@ResponseBody
	public TaotaoResult  checkData(@PathVariable String param,@PathVariable Integer type){
		//1.引入服务
		//2.注入服务
		//3.调用服务
		TaotaoResult result = registerservice.checkData(param, type);
		
		return result;
		
	}
	
	
	//url:/user/register
	//method:post
	//参数：表单（tbuser 来接收）
	//返回值：json  taotaoresult
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user){
		return registerservice.register(user);
	}
	
	
}
