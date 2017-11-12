package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.jedis.JedisClient;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private TbOrderMapper ordermapper;
	
	@Autowired
	private TbOrderShippingMapper shippingmapper;
	
	@Autowired
	private TbOrderItemMapper itemmapper;
	
	@Autowired
	private JedisClient jedisclient;
	
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;//生成订单号的key
	
	@Value("${ORDER_ID_GEN_INIT}")
	private String ORDER_ID_GEN_INIT;//生成订单号的初始值
	
	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	
	private String ORDER_ITEM_ID_GEN_KEY;

	@Override
	public TaotaoResult saveOrderInfo(OrderInfo info) {
		//生成一个订单号 redis 生城  需要注入jedisclient
		//判断ORDER_ID_GEN_KEY 是否存在  ，如果不存在 需要给初始值
		if(!jedisclient.exists(ORDER_ID_GEN_KEY)){
			jedisclient.set(ORDER_ID_GEN_KEY, ORDER_ID_GEN_INIT);
		}
		String orderId = jedisclient.incr(ORDER_ID_GEN_KEY).toString();//生成有初始值得订单号
		//补全订单的其他的属性值
		info.setOrderId(orderId);
		info.setPostFee("0");
		info.setStatus(1);//状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		info.setCreateTime(new Date());
		info.setUpdateTime(info.getCreateTime());
		//用户的id 应该在controller中调用SSO的服务直接设置
		//插入订单表
		ordermapper.insertSelective(info);
		
		List<TbOrderItem> orderItems = info.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//补全 订单项的其他的属性值
			String incr = jedisclient.incr(ORDER_ITEM_ID_GEN_KEY).toString();
			tbOrderItem.setId(incr);
			tbOrderItem.setOrderId(orderId);
			//插入订单项表
			itemmapper.insert(tbOrderItem);
		}
		
		//补全物流的其他的属性
		TbOrderShipping orderShipping = info.getOrderShipping();
		//插入物流表
		orderShipping.setCreated(info.getCreateTime());
		orderShipping.setOrderId(orderId);
		orderShipping.setUpdated(info.getCreateTime());
		shippingmapper.insert(orderShipping);
		//需要传递订单号码
		return TaotaoResult.ok(orderId);
	}

}
