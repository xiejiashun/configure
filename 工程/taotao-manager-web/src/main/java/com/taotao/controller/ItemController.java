package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import com.taotao.web.util.FastDFSClient;

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemservice;
	
	//图片路径的服务器的地址
	@Value("${TAOTAO_IMAGE_SERVER_URL}")
	private String TAOTAO_IMAGE_SERVER_URL;
	
	//分页查询商品的列表
	//url:'/item/list',method:'get'
	//参数：page,rows
	//返回值：json
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDatagridResult getItemList(Integer page,Integer rows){
		//1.引入服务
		//2.注入服务
		//3.调用
		return itemservice.getItemListByPage(page, rows);
	}
	
	
	//url:url：/pic/upload
	//参数：uploadFile
	//返回值：json
//	@RequestMapping("/pic/upload")
//	@ResponseBody
//	public Map<String, Object> uploadImage(MultipartFile uploadFile) {
//		
//		try {
//			//获取原文件的扩展名
//			String filename = uploadFile.getOriginalFilename();
//			String extName=filename.substring(filename.lastIndexOf(".")+1);//需要不带点的扩展名
//			//获取元文件的字节数组
//			byte[] bytes = uploadFile.getBytes();
//			
//			//创建fastdfsclient对象   要求：有配置文件
//			FastDFSClient fastDFSClient = new FastDFSClient("classpath:properties/fast_dfs.conf");
//			//调用方法上传图片
//			//返回的是路径 group1/M00/00/02/wKgZhVnMat2AQE1eAAIbczWvGxA093.jpg
//			String string = fastDFSClient.uploadFile(bytes, extName);
//			//还需要拼接全路径 交给页面（js三方的上传图片的插件）
//			String imageurl = TAOTAO_IMAGE_SERVER_URL+string;
//			
//			//将数据提供给页面
//			Map<String, Object> map = new HashMap<>();
//			map.put("error", 0);
//			map.put("url", imageurl);
//			return map;
//		} catch(Exception e){
//			Map<String, Object> map = new HashMap<>();
//			map.put("error",1);
//			map.put("message", "上传失败");
//			return map;
//		}
//	}
	
	
	//解决兼容性问题  produces:指定content-type
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";utf-8")
	@ResponseBody//默认返回的content-type:application/json; 火狐浏览器不支持
	//content-type:text/plain;火狐浏览器是支持的。
	public String uploadImage(MultipartFile uploadFile) {
		
		try {
			//获取原文件的扩展名
			String filename = uploadFile.getOriginalFilename();
			String extName=filename.substring(filename.lastIndexOf(".")+1);//需要不带点的扩展名
			//获取元文件的字节数组
			byte[] bytes = uploadFile.getBytes();
			
			//创建fastdfsclient对象   要求：有配置文件
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:properties/fast_dfs.conf");
			//调用方法上传图片
			//返回的是路径 group1/M00/00/02/wKgZhVnMat2AQE1eAAIbczWvGxA093.jpg
			String string = fastDFSClient.uploadFile(bytes, extName);
			//还需要拼接全路径 交给页面（js三方的上传图片的插件）
			String imageurl = TAOTAO_IMAGE_SERVER_URL+string;
			
			//将数据提供给页面
			Map<String, Object> map = new HashMap<>();
			map.put("error", 0);
			map.put("url", imageurl);
			String json = JsonUtils.objectToJson(map);
			
			return json;
		} catch(Exception e){
			Map<String, Object> map = new HashMap<>();
			map.put("error",1);
			map.put("message", "上传失败");
			String json = JsonUtils.objectToJson(map);
			return json;
		}
	}
	
	
	//url:/item/save
	//参数：表单 （tbitem item  String desc）
	//返回值  json
	
	@RequestMapping("/item/save")
	@ResponseBody
	public TaotaoResult saveItemAndDesc(TbItem item,String desc){
		return itemservice.saveItemAndItemDesc(item, desc);
	}
	
	public static void main(String[] args) {
		double a =1.3d;//单位 元
		double aa =130d;//单位 分
		int num=3;
		
		float b = 1.3f;
		int numf  =3;
		System.out.println(aa*num);
		System.out.println(numf*b);
	}
}
