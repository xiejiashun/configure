package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.sso.service.UserLoginService;

public class LoginInterceptor implements HandlerInterceptor {
	@Autowired
	private UserLoginService loginservice;
	
	@Value("${SSO_URL}")
	private String SSO_URL;

	//在进入目标放法之前执行
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//身份的校验
		//1.从cookie种获取token 
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		//2.调用sso的服务 获取用户的信息  （引入依赖，引入服务，注入服务）
		//2.调用SSO的服务获取用户的信息
		TaotaoResult result = loginservice.getUserByToken(token);
		if(result.getStatus()!=200){
			//3.判断用户是否存在  如果不存在     重新定向到用户的登录页面
			response.sendRedirect(SSO_URL+"/page/login?url="+request.getRequestURL().toString());
			return false;
		}
		System.out.println("如果放行就执行方法》》》》》》》》》》");
		//如果登录就放行
		request.setAttribute("USER_INFO", result.getData());
		return true;
	}

	//返回modelandview 之前执行在进入目标方法之后执行
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	//返回页面之前
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
