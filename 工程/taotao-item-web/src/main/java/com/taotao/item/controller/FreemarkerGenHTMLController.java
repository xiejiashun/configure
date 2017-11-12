package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.RespectBinding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

/**
 * 用于测试发送请求，接收请求调用freemarker方法生产静态页面
 * @title
 * @description
 * @author ljh 
 * @version 1.0
 */
@RestController
public class FreemarkerGenHTMLController {
	
	@Autowired
	private FreeMarkerConfigurer config;
	
	//模板+ 数据 = 静态页面
	@RequestMapping("/genhtml")
	public String genthtml(){
		//1.创建configuration对象
		Configuration configuration = config.getConfiguration(); 
		//2.设置模板所在的路径
		//3.设置模板的默认的字符编码    以上的3个步骤可以由spring管理
		
		
		//4.手动创建模板文件  加载模板文件    （）
		try {
			Template template = configuration.getTemplate("template.ftl");
			
			//5.设置数据集
			
			Map<String, Object> model = new HashMap<>();
			model.put("hello", "spring freemarker");
			
			//6.创建流对象
			Writer out = new FileWriter(new File("G:/freemarker/result.html"));
			
			//7.调用方法输出 
			template.process(model, out);
			//8.流关闭
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		return null;
	}
	
}
