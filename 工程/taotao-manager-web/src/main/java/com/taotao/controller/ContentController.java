package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;

@Controller
public class ContentController {
	
	
	@Autowired
	private ContentService contentservice;
	
	
	//url：/content/query/list
	//参数：categorId page  rows
	//method：get
	//返回值 json easyuidatagrid
	
	@RequestMapping(value="/content/query/list",method=RequestMethod.GET)
	@ResponseBody
	public EasyUIDatagridResult getContentCategoryBy(Long categoryId,Integer page,Integer rows){
		//1.引入服务
		//2.注入
		//3.调用
		return contentservice.getContentListBy(categoryId, page, rows);
	}
	
	//url:/content/save
	//参数：表单 tbcontent来接受
	//method post
	//返回值：json taotaorsult
	
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult save(TbContent content){
		return contentservice.saveContent(content);
	}
	
	
	
	
}
