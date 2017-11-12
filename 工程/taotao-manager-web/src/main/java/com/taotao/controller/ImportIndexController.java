package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
/**
 * 用于导入索引库的controller
 * @title
 * @description
 * @author ljh 
 * @version 1.0
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;
@Controller
public class ImportIndexController {
	@Autowired
	private SearchItemService searchitemservice;
	
	//导入索引库
	@RequestMapping("/importAllIndex")
	@ResponseBody
	public TaotaoResult importAllIndex() throws Exception{
		//1.引入服务
		//2.注入服务
		//3.调用服务
		return searchitemservice.importAllToIndex();
	}
}
