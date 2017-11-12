package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.search.service.SearchItemService;

/**
 * 监听器 作用：接收消息（商品的id） 根据商品的id查询数据库的（搜索时）商品的数据 使用solrj 更新索引库
 * 
 * @title
 * @description
 * @author ljh
 * @version 1.0
 */
public class ItemChangeListener implements MessageListener {

	@Autowired
	private SearchItemService itemservice;

	@Override
	public void onMessage(Message message) {

		if (message instanceof TextMessage) {
			try {
				// 1.接收消息
				TextMessage message2 = (TextMessage) message;
				String itemidtext = message2.getText();
				Long itemId = Long.valueOf(itemidtext);
				// 2.根据商品的id 查询数据 库商品数据 更新索引库 （service层实现业务逻辑） ；这里只需要注入service
				// 调用方法实现 即可。
				itemservice.updateSearchItemIndex(itemId);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
