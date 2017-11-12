package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

/**
 * 用户注册的接口服务
 * @title
 * @description
 * @author ljh 
 * @version 1.0
 */
public interface UserRegisterService {
	/**
	 * 校验数据 是否可用
	 * @param param 要检验的数据
	 * @param type  校验数据的类型  1 2 3 
	 * @return  包含数据data  true  false
	 */
	public TaotaoResult checkData(String param,Integer type);
	
	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	public TaotaoResult register(TbUser user);
}
