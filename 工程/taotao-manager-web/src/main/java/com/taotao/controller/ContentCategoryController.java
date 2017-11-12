package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TreeNode;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.pojo.TbContentCategory;

@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService service;
	
	//url：/content/category/list
	//method: get
	//参数：id
	//返回值： json
	
	@RequestMapping(value="/content/category/list",method=RequestMethod.GET)
	@ResponseBody
	public List<TreeNode> getContentCategoryById(@RequestParam(value="id",defaultValue="0") Long parentId){
		//1.引入服务
		//2.注入
		//3.调用
		return service.getContentCategoryListByParentId(parentId);
	}
	
	//添加分类
	
	//url:/content/category/create
	//method:post
	//参数:parentid name
	//返回值：json  taotaoresult
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult saveContentCategory(TbContentCategory category){
		//引入服务
		//注入
		//调用
		
		return service.saveContentCategory(category);
	}
	
}
