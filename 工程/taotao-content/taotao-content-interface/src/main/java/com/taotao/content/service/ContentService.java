package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	//根据分类id 分页查询该分类下的所有的内容列表
	
	public EasyUIDatagridResult getContentListBy(Long categoryId,Integer page,Integer rows);
	
	//添加内容  
	
	public TaotaoResult saveContent(TbContent content);
	//根据分类的ID 查询该分类下的所有的内容列表
	
	public List<TbContent> getContentListByCategoryId(Long categoryId);
}
