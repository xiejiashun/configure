package com.taotao.search.execption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
/**
 * 全局异常处理器 （处理全局异常）
 * @title
 * @description
 * @author ljh 
 * @version 1.0
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		//1.有异常，将异常信息写入日志文件（使用log4j处理）  这里：打印
		
		System.out.println("有异常啦：");
		
		ex.printStackTrace();
		
		
		//2.通知开发人员（发短信：调用第三方的发短信的接口 ，发邮件,javamail） 去修改代码 .打印
		
		System.out.println("通知开发人员");
		
		
		
		//3.给用户一个友好的提示页面：您的网络有异常 ，请重试。
		ModelAndView mvAndView  = new ModelAndView();
		mvAndView.addObject("message", "您的网络有异常 ，请重试");
		mvAndView.setViewName("error/exception");
		
		//4.返回 一个页面（modelandview）
		return mvAndView;
	}

}
