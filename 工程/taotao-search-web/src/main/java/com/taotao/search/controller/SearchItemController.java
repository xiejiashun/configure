package com.taotao.search.controller;

import javax.jws.WebParam.Mode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchItemService;

/**
 * 搜索相关的controller
 * @title
 * @description
 * @author ljh 
 * @version 1.0
 */
@Controller
public class SearchItemController {
	@Autowired
	private SearchItemService searchItemService;
	
	@Value("${ITEM_ROWS}")
	private Integer ITEM_ROWS;
	//url:/search
	//参数：q  page
	//返回值：string 
	@RequestMapping(value="/search")
	public String search(@RequestParam(value="q") String queryString,@RequestParam(defaultValue="1") Integer page,Model model) throws Exception{
//		int i=9/0;
		
		//1.引入服务
		//2.注入服务
		//3.调用服务  返回的是searchresult(包括总记录数 商品的列表 总页数)
		
		//解决乱码问题
		queryString = new String(queryString.getBytes("iso8859-1"),"utf-8");
		
		SearchResult result = searchItemService.search(queryString, page, ITEM_ROWS);
		//4.传递数据到页面中
		
//		${totalPages}
		model.addAttribute("totalPages", result.getPageCount());
//
//		${query}
		model.addAttribute("query", queryString);
//
//		${itemList}
		
		model.addAttribute("itemList", result.getItemList());
//
//		${page}
		model.addAttribute("page", page);
		return "search";
	}
}
