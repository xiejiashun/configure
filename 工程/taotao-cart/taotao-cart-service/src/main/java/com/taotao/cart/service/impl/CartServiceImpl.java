package com.taotao.cart.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.cart.jedis.JedisClient;
import com.taotao.cart.service.CartService;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private JedisClient jedisclient;
	
	@Value("${CART_REDIS_PRE_KEY}")
	private String CART_REDIS_PRE_KEY;

	@Override
	public TaotaoResult addCartItem(Long userId, TbItem item, Integer num) {
		//1.注入jedisclient 
		
		//2.获取购物车的列表     --->通过 hash :直接通过 key--field 获取商品的数据
		TbItem itemadd = getItemByUserIdAndItemId(userId,item.getId());
		//3.判断 要添加的商品是否在 购物车中  
		if(itemadd!=null){
			//4.如果 要添加的商品在购物车中,直接商品的数量相加   
			itemadd.setNum(itemadd.getNum()+num);
			//重新设置到redis中
			jedisclient.hset(CART_REDIS_PRE_KEY+":"+userId, item.getId()+"", JsonUtils.objectToJson(itemadd));
		}else{
			//5.如果要添加的商品的不在购物车中,直接添加商品到购物车
			// 图片设置一张    
			if(StringUtils.isNotBlank(item.getImage())){
				//切割图片 取一张
				item.setImage(item.getImage().split(",")[0]);
			}
			// 数量要设置
			item.setNum(num);
			//6.重新设置到redis中
			jedisclient.hset(CART_REDIS_PRE_KEY+":"+userId, item.getId()+"", JsonUtils.objectToJson(item));
		}
		return TaotaoResult.ok();
	}
	/**
	 * 从redis中获取商品的数据
	 * @param userId
	 * @param id
	 */
	private TbItem getItemByUserIdAndItemId(Long userId, Long id) {
		//获取商品数据的json
		String string = jedisclient.hget(CART_REDIS_PRE_KEY+":"+userId, id+"");
		//判断是否有值  
		if(StringUtils.isNotBlank(string)){
			return JsonUtils.jsonToPojo(string, TbItem.class);
		}
		//说明 redis购物车中没有要添加的商品
		return null;
	}
	@Override
	public List<TbItem> getCartListByUserId(Long userId) {
		
		Map<String, String> map = jedisclient.hgetAll("CART_REDIS_PRE"+":"+userId);
		
		//循环遍历 map  封装到list中
		List<TbItem> list = new ArrayList<>();
		
		//判断 map是否为空
		if(map!=null){
			Set<Entry<String,String>> entrySet = map.entrySet();
			
			for (Entry<String, String> entry : entrySet) {
				//entry.getKey()//商品的id
				//entry.getValue()//商品数据的JSON格式字符串
				list.add(JsonUtils.jsonToPojo(entry.getValue(), TbItem.class));
			}
		}
		return list;//最好不要返回一个null 最好返回空的集合对象即可
	}
	@Override
	public TaotaoResult updateCartItem(Long userId, Long itemId, Integer num) {
		//1.获取根据key,field 获取商品的数据对象
		TbItem tbItem = getItemByUserIdAndItemId(userId,itemId);
		if(tbItem!=null){
			//2.判断 要修改的商品  是否存在  如果存在  就修改数量  
			tbItem.setNum(num);
			//3.设置回redis 
			jedisclient.hset(CART_REDIS_PRE_KEY+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
		}
		return TaotaoResult.ok();
	}
	@Override
	public TaotaoResult deleteCartItem(Long userId, Long itemId) {
		Long long1 = jedisclient.hdel(CART_REDIS_PRE_KEY+":"+userId, itemId+"");
		
		return TaotaoResult.ok();
	}

}
