package com.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * 监听器 
 * @title
 * @description
 * @author ljh 
 * @version 1.0
 */
public class ItemChangeGenHTMLListener implements MessageListener {
	
	@Autowired
	
	private FreeMarkerConfigurer config;
	@Autowired
	private ItemService itemservice;
	@Override
	public void onMessage(Message message) {
		
		if(message instanceof TextMessage){
			try {
				//1.接收消息 
				TextMessage message2 =  (TextMessage)message;
				
				//2.获取消息的内容 就是商品的id
				String string = message2.getText();
				Long itemId = Long.valueOf(string);
				//3.调用商品的服务 获取最新的商品的数据（基本信息和描述信息）----》相当于数据集 
					//1.引入服务 
					//2.注入服务
					//3.调用更具商品的id查询商品的基本信息，商品的描述信息
				TbItem tbItem = itemservice.getItemById(itemId);
				
				TbItemDesc tbItemDesc = itemservice.getItemDesc(itemId);
				
				
				//4.调用freemarker的方法 生成静态页面（数据集 和 模板）
				genrateHTMLFreemarker("item.ftl",tbItem,tbItemDesc);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}

	}
	/**
	 * 根据数据集 和模板文件的名称  生成静态的页面
	 * @param templateName
	 * @param item
	 * @param desc
	 */
	private void genrateHTMLFreemarker(String templateName,TbItem item,TbItemDesc desc) throws Exception{
		//1.创建configuration对象 
		Configuration configuration = config.getConfiguration();
		//2.获取模板文件对象 （手动创建  加载模板文件）  模板 
		
		Template template = configuration.getTemplate(templateName);
		
		//3.创建数据集        数据集
		Map<String, Object> model = new HashMap<>();
		model.put("item", new Item(item));
		model.put("itemDesc", desc);
		//4.创建输出流  输出到指定的静态页面文件中
		Writer writer = new FileWriter(new File("G:/freemarker/item/"+item.getId()+".html"));
		template.process(model, writer);
		//5.关闭流
		writer.close();
	}

}
