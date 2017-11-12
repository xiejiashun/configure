package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.IDUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.manager.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper mapper;

	@Autowired
	private TbItemDescMapper descmapper;
	
	
	@Autowired
	private JedisClient client;

	// 注入destination
	// 注入 jmstemplate
	
	
	@Value("${ITEM_INFO_KEY}")
	private String ITEM_INFO_KEY;
	
	@Value("${EXPIRE_ITEM_KEY_TIME}")
	private Integer EXPIRE_ITEM_KEY_TIME;

	@Autowired
	private JmsTemplate jmstemplate;

	@Resource(name = "topicDestination")
	private Destination destination;

	@Override
	public EasyUIDatagridResult getItemListByPage(Integer page, Integer rows) {
		// 1.注入mapper
		// 2.设置分页
		PageHelper.startPage(page, rows);
		// 3.创建example对象 可以设置查询的条件（如果查询所有的数据，可以不设置）
		TbItemExample example = new TbItemExample();// select * from tbitem ;
		// 4.调用mapper的查询方法 返回是list 第一个查询被分页
		List<TbItem> list = mapper.selectByExample(example);

		// 5.获取分页信息
		PageInfo<TbItem> info = new PageInfo<>(list);
		// 6.封装到EasyUIDatagridResult里 返回
		EasyUIDatagridResult result = new EasyUIDatagridResult();
		result.setRows(info.getList());
		result.setTotal(info.getTotal());
		return result;
	}

	@Override
	public TaotaoResult saveItemAndItemDesc(TbItem item, String desc) {
		// 1.注入mapper

		// 2.生成商品的id
		final long itemId = IDUtils.genItemId();

		// 3.添加商品基本信息 （补全商品的基本信息的其他属性）
		item.setId(itemId);
		item.setStatus((byte) 1);// 商品状态，1-正常，2-下架，3-删除
		item.setCreated(new Date());
		item.setUpdated(item.getCreated());

		mapper.insertSelective(item);

		// 4.添加 商品的描述信息（补全其他的属性）
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemDesc(desc);
		itemDesc.setItemId(itemId);
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getCreated());
		descmapper.insertSelective(itemDesc);
		// 5.返回taotaoresult

		// 发送消息

		jmstemplate.send(destination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				//发送商品的id
				return session.createTextMessage(itemId+"");
			}
		});

		return TaotaoResult.ok();
	}

	@Override
	public TbItem getItemById(Long id) {
		//添加缓存不能影响正常的业务逻辑
		
		//判断如果有缓存直接返回
		try {
			String string = client.get(ITEM_INFO_KEY+":"+id+":BASE");
			
			if(StringUtils.isNotBlank(string)){
				client.expire(ITEM_INFO_KEY+":"+id+":BASE", EXPIRE_ITEM_KEY_TIME);//一天重新设置有效期
				return JsonUtils.jsonToPojo(string, TbItem.class);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
			
		
	
		TbItem tbItem = mapper.selectByPrimaryKey(id);
		
		//添加缓存 
//		ITEM_INFO:123456:BASE
		//1.注入jedisclient
		try {
			//2.直接设置缓存
			client.set(ITEM_INFO_KEY+":"+id+":BASE", JsonUtils.objectToJson(tbItem));
			//设置有效期
			client.expire(ITEM_INFO_KEY+":"+id+":BASE", EXPIRE_ITEM_KEY_TIME);//一天
		} catch (Exception e) {
			e.printStackTrace();
		}
		//3.返回
		
		
		
		return tbItem;
	}

	@Override
	public void deleteById(Long id) {
		mapper.deleteByPrimaryKey(id);
		descmapper.deleteByPrimaryKey(id);

	}

	@Override
	public void updateItem(TbItem item) {
		mapper.updateByPrimaryKeySelective(item);

	}

	@Override
	public TbItemDesc getItemDesc(Long id) {
		//判断如果有缓存直接返回
		try {
			String string = client.get(ITEM_INFO_KEY+":"+id+":DESC");
			
			if(StringUtils.isNotBlank(string)){
				client.expire(ITEM_INFO_KEY+":"+id+":DESC", EXPIRE_ITEM_KEY_TIME);//一天重新设置有效期
				return JsonUtils.jsonToPojo(string, TbItemDesc.class);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//添加缓存
		TbItemDesc desc = descmapper.selectByPrimaryKey(id);
		
		//添加缓存 
//		ITEM_INFO:123456:BASE
		//1.注入jedisclient
		try {
			//2.直接设置缓存
			client.set(ITEM_INFO_KEY+":"+id+":DESC", JsonUtils.objectToJson(desc));
			//设置有效期
			client.expire(ITEM_INFO_KEY+":"+id+":DESC", EXPIRE_ITEM_KEY_TIME);//一天
		} catch (Exception e) {
			e.printStackTrace();
		}
		//3.返回
		
		return desc;
	}


}
