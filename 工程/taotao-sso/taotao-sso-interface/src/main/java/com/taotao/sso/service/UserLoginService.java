package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;

/**
 * 用户登录服务接口
 * @title
 * @description
 * @author ljh 
 * @version 1.0
 */
public interface UserLoginService {
	
	public TaotaoResult  login(String username,String password);
	/**
	 * 根据token 获取用户信息
	 * @param token
	 * @return
	 */
	public TaotaoResult getUserByToken(String token);
}
