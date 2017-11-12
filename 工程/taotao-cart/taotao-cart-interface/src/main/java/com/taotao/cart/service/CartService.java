package com.taotao.cart.service;

import java.util.List;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface CartService {
	/**
	 * 添加购物车  使用redis 添加 （ key:用户的id--field:商品的id --value:商品的JSON）
	 * @param userId
	 * @param item
	 * @param num
	 * @return
	 */
	public TaotaoResult addCartItem(Long userId,TbItem item,Integer num);
	/**
	 * 获取某一个用户的购物车的商品的列表
	 * @param userId
	 * @return
	 */
	public List<TbItem> getCartListByUserId(Long userId);
	/**
	 * 更新指定用户的购物车的商品的数量
	 * @param userId
	 * @param itemId
	 * @param num  更新后的数量
	 * @return
	 */
	public TaotaoResult updateCartItem(Long userId,Long itemId,Integer num); 
	
	/**
	 * 删除指定的商品
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public TaotaoResult deleteCartItem(Long userId,Long itemId);
}
