package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.service.UserRegisterService;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

	@Autowired
	private TbUserMapper mapper;
	
	@Override
	public TaotaoResult checkData(String param, Integer type) {
		//1.注入一个mapper
		
		//2.判断  type 的类型  1 2 3 
		if(type==1){
			//3.如果是1 校验用户名
			if(StringUtils.isBlank(param)){
				return TaotaoResult.ok(false);//数据不可用
			}
			//.创建example 设置查询的条件
			TbUserExample example = new TbUserExample();
			example.createCriteria().andUsernameEqualTo(param);
			//执行查询
			List<TbUser> list = mapper.selectByExample(example);
			//判断  查询的结果  如果有 说明 用户名或者手机 或者邮箱已经被注册过了，数据不可以用。
			if(list!=null && list.size()>0){//查到了。说明数据不可以用
				return TaotaoResult.ok(false);
			}else{
				return TaotaoResult.ok(true);
			}
			
			
			
		}else if(type==2){
			//4.如果是2 校验电话号码
			if(StringUtils.isNotBlank(param)){
				TbUserExample example = new TbUserExample();
				example.createCriteria().andPhoneEqualTo(param);
				
				
				List<TbUser> list = mapper.selectByExample(example);
				//判断  查询的结果  如果有 说明 用户名或者手机 或者邮箱已经被注册过了，数据不可以用。
				if(list!=null && list.size()>0){//查到了。说明数据不可以用
					return TaotaoResult.ok(false);
				}else{
					return TaotaoResult.ok(true);
				}
			}
			
			return TaotaoResult.ok(true);
		}else if(type==3){
			//5.如果是3 校验邮箱地址
			if(StringUtils.isNotBlank(param)){
				TbUserExample example = new TbUserExample();
				example.createCriteria().andEmailEqualTo(param);
				
				
				List<TbUser> list = mapper.selectByExample(example);
				//判断  查询的结果  如果有 说明 用户名或者手机 或者邮箱已经被注册过了，数据不可以用。
				if(list!=null && list.size()>0){//查到了。说明数据不可以用
					return TaotaoResult.ok(false);
				}else{
					return TaotaoResult.ok(true);
				}
			}
			
			return TaotaoResult.ok(true);
			
		}else{
			//6.否则  错误的状态码  400的状态码 直接返回
			return TaotaoResult.build(400, "数据参数错误");
		}
			//7.创建example 设置查询的条件
			
			//8.执行查询
			
			//9.判断  查询的结果  如果有 说明 用户名或者手机 或者邮箱已经被注册过了，数据不可以用。
			
			//10.返回
		
	}

	@Override
	public TaotaoResult register(TbUser user) {
		//1.校验用户名和密码不能为空  
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
			return TaotaoResult.build(400, "请校验数据后请再提交数据");
		}
			
		
		//2.校验数据是否可用   用户名不能重复
		TaotaoResult result = checkData(user.getUsername(),1);
		if(!(boolean)result.getData()){//如果为false 
			return TaotaoResult.build(400, "用户名已经被注册了");
		}
		//3.校验电话号码 可以为空，一旦不为空    数据不能重复
		if(StringUtils.isNotBlank(user.getPhone())){
			TaotaoResult result2 = checkData(user.getPhone(),2);
			if(!(boolean)result2.getData()){//如果为false 
				return TaotaoResult.build(400, "电话号码已经被注册了");
			}
			
		}
		
		//4.校验邮箱    可以为空，一旦不为空    数据不能重复
		
		if(StringUtils.isNotBlank(user.getEmail())){
			TaotaoResult result2 = checkData(user.getEmail(),3);
			if(!(boolean)result2.getData()){//如果为false 
				return TaotaoResult.build(400, "邮箱已经被注册了");
			}
			
		}
		
		//5.插入数据  mapper的insert
		//需要对密码MD5加密
		String password = user.getPassword();
		
		String md5password = DigestUtils.md5DigestAsHex(password.getBytes());//加密后的密码
		
		user.setPassword(md5password);
		
		//补全 创建时间 和更新时间
		
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		mapper.insertSelective(user);
		
		
		//6.返回
		
		return TaotaoResult.ok();
	}

}
