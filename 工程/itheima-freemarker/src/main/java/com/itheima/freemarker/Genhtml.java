package com.itheima.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

public class Genhtml {
	
	//生成静态页面的方法
	//模板    +  数据集  
	@Test
	public void testFreemarker() throws Exception{
		//1.创建configuration 配置
		Configuration configuration = new Configuration(Configuration.getVersion());
		//2.设置模板所在的路径 
		configuration.setDirectoryForTemplateLoading(new File("C:/Users/ThinkPad/workspace-taotao-12/itheima-freemarker/src/main/resources/template"));
		//3.设置模板的字符编码 utf-8
		configuration.setDefaultEncoding("utf-8");
		
		//4.手动创建模板文件   程序加载模板文件 (一般 模板文件的后缀官方推荐.ftl )  相对于模板所在的路径的相对路径
		Template template = configuration.getTemplate("hello.htm");
		
		//5.创建 map (用于存放数据集) 当然也可以是POJO
		
		Map<String, Object> model = new HashMap<>();
		//通过 设置简单的数据类型
		model.put("hello", "hello rvmb");
		
		
		
		//POJO 
		
		model.put("person1", new Person(1001l, "奥巴马"));
		model.put("person2", new Person(1002l, "奥特曼"));
		model.put("person3", new Person(1003l, "奥利奥"));
		
		
		//LIST
		
		List<Person> list = new ArrayList<>();
		
		list.add(new Person(1001l, "小乔"));
		list.add(new Person(1002l, "大乔"));
		list.add(new Person(1003l, "貂蝉"));
		
		
		model.put("list", list);
		
		
		//MAP
		
		Map<String, Object> map = new HashMap<>();
		map.put("m1", new Person(1001l, "三毛"));
		map.put("m2", new Person(1002l, "张佳嘉"));
		map.put("m3", new Person(1003l, "鲁迅"));
		
		model.put("map", map);
		
		//输出日期
		
		model.put("date", new Date());
		
		//null 值的处理
		
		model.put("keynull", "世界上本有路，走的人多了就没路了");
		model.put("template", "模板include");
		
		
		//6.创建流对象  输出到指定的文件中
		Writer  output = new FileWriter(new File("C:/Users/ThinkPad/workspace-taotao-12/itheima-freemarker/src/main/resources/html/result.html"));
		//7.调用方法 输出文件
		template.process(model, output);
		//8.关闭流
		output.close();
		
	}
	
}
