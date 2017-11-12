package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.util.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.Ad1Node;

@Controller
public class IndexController {
	
	@Autowired
	private ContentService contentservice;
	
	@Value("${CATEGORY_ID}")
	private Long CATEGORY_ID;
	
	@RequestMapping("/index")
	public String showIndex(Model model){
		//1.引入服务
		//2.注入服务
		//3.调用服务  List<tbcontent>
		List<TbContent> list = contentservice.getContentListByCategoryId(CATEGORY_ID);
		
		//4.转成List<Ad1Node> 再转成JSON数据
		List<Ad1Node> nodes = new ArrayList<>();
		for (TbContent tbContent : list) {
			Ad1Node node = new Ad1Node();
			node.setAlt(tbContent.getSubTitle());
			node.setHeight("240");
			node.setHeightB("240");
			node.setHref(tbContent.getUrl());
			node.setSrc(tbContent.getPic());
			node.setSrcB(tbContent.getPic2());
			node.setWidth("670");
			node.setWidthB("550");
			nodes.add(node);
		}
		String jsonnodes = JsonUtils.objectToJson(nodes);
		
		//5.传递数据到页面中
		model.addAttribute("ad1Node", jsonnodes);
		return "index";
	}
	
}	
