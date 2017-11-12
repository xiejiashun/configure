package com.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemservice;
	
	//根据商品的id 查询商品的基本信息和商品的描述信息  给页面
	
	@RequestMapping("/item/{itemId}")
	public String getInfo(@PathVariable Long itemId,Model model ){
		//引入服务
		//注入服务
		//调用服务的方法  获取到的是tbitem  还要转换成 item
		TbItem tbItem = itemservice.getItemById(itemId);
		
		Item item  = new Item(tbItem);
		
		TbItemDesc tbItemDesc = itemservice.getItemDesc(itemId);
		
		
		//传递数据到页面中
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", tbItemDesc);
		return "item";
	}
}
