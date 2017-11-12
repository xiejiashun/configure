package com.taotao.sso.service.impl;

import java.util.List;
import java.util.UUID;

import javax.swing.tree.ExpandVetoException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.sso.service.UserLoginService;

@Service
public class UserLoginServiceImpl implements UserLoginService {
	
	@Autowired
	private TbUserMapper usermapper;
	@Autowired
	private JedisClient jedisclient;
	
	@Value("${SESSION_KEY}")
	private String SESSION_KEY;
	
	@Value("${EXPIRE_TIME}")
	private Integer EXPIRE_TIME;
	

	@Override
	public TaotaoResult login(String username, String password) {
		//1.校验用户名和密码不能为空
		if(StringUtils.isBlank(username)|| StringUtils.isBlank(password)){
			return TaotaoResult.build(400, "用户名或者密码错误");
		}
		
		//2.创建example 设置查询的条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		criteria.andPasswordEqualTo(DigestUtils.md5DigestAsHex(password.getBytes()));
		
		//3.执行查询
		List<TbUser> list = usermapper.selectByExample(example);
		
		//4. 判断 没有数据 登录失败
		if(list==null || list.size()==0){
			
			return TaotaoResult.build(400, "用户名或者密码错误");
		}
		
		//5.说明登录成功   生成唯一的key UUID生成
		String token = UUID.randomUUID().toString();
		
		
		//6.存储用户的信息的JSON到redis中
		TbUser tbUser = list.get(0);
		tbUser.setPassword(null);//设置为null
		jedisclient.set(SESSION_KEY+":"+token, JsonUtils.objectToJson(tbUser));//为了管理方便
		
		//7.将token 存储到cookie中   应该在controller中进行设置
		
		
		
		//8.设置key的有效期  模拟成半个小时
		jedisclient.expire(SESSION_KEY+":"+token, EXPIRE_TIME);
		
		//9.返回
		return TaotaoResult.ok(token);
	}


	@Override
	public TaotaoResult getUserByToken(String token) {
		//1.根据token 使用jedisclient 获取用户的信息
		String jsonString = jedisclient.get(SESSION_KEY+":"+token);
		if(StringUtils.isNotBlank(jsonString)){
			//2.如果数据不为空   说明用户已经登录   重新设置该用户的key的有效期
			jedisclient.expire(SESSION_KEY+":"+token, EXPIRE_TIME);
			//需要转成TBuser对象传递到页面
			
			return TaotaoResult.ok(JsonUtils.jsonToPojo(jsonString, TbUser.class));
		}else{
			//3.如果数据为空 说明用户校验失败 用户已经过期
			return TaotaoResult.build(400, "用户已经过期了");
		}
		//4.返回
	}

}
