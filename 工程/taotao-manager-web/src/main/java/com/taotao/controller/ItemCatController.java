package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TreeNode;
import com.taotao.service.ItemCatService;

@Controller
public class ItemCatController {
	
	@Autowired
	private ItemCatService catservice;
	
	
	//根据分类的父节点查询父节点的所有的子节点列表展示在页面
	//url:/item/cat/list
	//参数：id
	//返回值：json
	
	//展示顶层的列表时 没有传递值 需要设置默认的值
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<TreeNode> getItemCatListByParentId(@RequestParam(value="id",defaultValue="0") Long parentId){
		//1.引入服务
		//2.注入服务
		//3.调用服务
		return catservice.getItemCatListByParentId(parentId);
	}
	
	
}
