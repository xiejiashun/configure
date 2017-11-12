package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

/**
 * restful web service 接口
 * @title
 * @description
 * @author ljh 
 * @version 1.0
 */

@RestController//restcontroller 实际上就是相当于开启了controller 和 responsebody 两个注解  一般用在接口的开发
public class ItemInterfaceController {
	
	
	@Autowired
	private ItemService itemservice;
	
	//url:/rest/interface/item      增加  201
	//method:POST
	@RequestMapping(value="/rest/interface/item",method=RequestMethod.POST)
	public ResponseEntity<Void> saveItem(TbItem item,String desc){
		
		try {
			itemservice.saveItemAndItemDesc(item, desc);
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		} catch (Exception e) {
			e.printStackTrace();
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
	}
	
	//url:/rest/interface/item/{id} 删除  204
	//method:DELETE
	
	@RequestMapping(value="/rest/interface/item/{id}",method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		try {
			itemservice.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);//删除  204
		} catch (Exception e) {
			e.printStackTrace();
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		
		
	}
	//url:/rest/interface/item   修改
	//method: put
	
	
	@RequestMapping(value="/rest/interface/item",method=RequestMethod.PUT)
	public ResponseEntity<Void> updateItem(TbItem item){
		try {
			itemservice.updateItem(item);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);//删除  204
		} catch (Exception e) {
			e.printStackTrace();
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	//url:/rest/interface/item/{id}  查询  200
	//method：get
	
	@RequestMapping(value="/rest/interface/item/{id}",method=RequestMethod.GET)
	public ResponseEntity<TbItem> getItemById(@PathVariable Long id){
		//1.引入商品服务
				//2.注入服务
				//3.调用服务
		TbItem tbItem;
		try {
			tbItem = itemservice.getItemById(id);
			//return ResponseEntity.status(HttpStatus.OK).body(tbItem);
			return ResponseEntity.ok(tbItem);
		} catch (Exception e) {
			//4.返回
			e.printStackTrace();
			//出错 返回 500
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	
}
