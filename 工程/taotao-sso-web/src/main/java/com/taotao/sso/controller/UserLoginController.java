package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.sso.service.UserLoginService;

/**
 * 用户登录的controller
 * @title
 * @description
 * @author ljh 
 * @version 1.0
 */
@Controller
public class UserLoginController {
	
	@Autowired
	private UserLoginService loginservice;
	
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	
	
	//url:/user/login
	//参数 username password
	//method:post
	//返回值：json
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username,String password,HttpServletRequest request,HttpServletResponse response){
		//1.引入服务
		//2.注入
		//3.调用服务
		TaotaoResult taotaoResult = loginservice.login(username, password);
		if(taotaoResult.getStatus()==200){
			//4.需要将token 设置到cookie中  cookie需要跨域
			CookieUtils.setCookie(request, response, TT_TOKEN_KEY, (String)taotaoResult.getData());
		}
		//5.返回
		return taotaoResult;
	}
	
	//url:/user/token/{token}
	//参数:token
	//返回值 json
	//method:get
	
	@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody//string：content-type:text/plain; 要改成 application/json;
	public String getUserByToken(@PathVariable String token,String callback){
		//1.判断是否为jsonp请求，如果是 jsonp的处理
		TaotaoResult result = loginservice.getUserByToken(token);
		if(StringUtils.isNotBlank(callback)){
			//伪装成类似于fun({"id":1});的格式的json数据
			String jsonpresult =callback+"("+ JsonUtils.objectToJson(result)+");";
			//返回
			return jsonpresult;
		}
		//2.如果不是 按照原来的方式
		//TaotaoResult result = loginservice.getUserByToken(token);
		return JsonUtils.objectToJson(result);
	}
}
