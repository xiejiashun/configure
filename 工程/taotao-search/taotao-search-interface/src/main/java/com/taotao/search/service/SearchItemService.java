package com.taotao.search.service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;

public interface SearchItemService {
	/**
	 * 导入所有的数据到索引库中
	 * @return
	 */
	public TaotaoResult importAllToIndex()  throws Exception;
	/**
	 * 分页查询商品列表
	 * @param queryString 主查询的条件
	 * @param page 当前的页码
	 * @param rows 每页显示的行
	 * @return
	 */
	public SearchResult search(String queryString,Integer page,Integer rows) throws Exception;
	
	/**
	 * 更新索引库
	 * @param itemId 更具商品的id
	 * @return
	 */
	public TaotaoResult updateSearchItemIndex(Long itemId) throws Exception;
}
