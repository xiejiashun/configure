package com.taotao.service;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
	/**
	 * 根据分页的页码 和每页显示的行数分页查询
	 * @param page
	 * @param rows
	 * @return
	 */
	public EasyUIDatagridResult getItemListByPage(Integer page,Integer rows);
	/**
	 * 保存商品
	 * @param item 商品的基本信息
	 * @param desc 商品的描述
	 * @return
	 */
	public TaotaoResult saveItemAndItemDesc(TbItem item,String desc);
	
	//根据商品的id 查询商品基本信息
	
	public TbItem getItemById(Long id);
	//获取商品的描述信息
	public TbItemDesc getItemDesc(Long id);
	
	//根据商品的id 删除
	
	public void deleteById(Long id);
	
	//更新商品的基本信息
	
	public void updateItem(TbItem item);
	
	
}
