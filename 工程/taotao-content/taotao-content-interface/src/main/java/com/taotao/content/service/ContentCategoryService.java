package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TreeNode;
import com.taotao.pojo.TbContentCategory;

public interface ContentCategoryService {
	/**
	 * 根据父节点的id查询该父节点所有的子节点列表（内容分类）
	 * @param parentId
	 * @return
	 */
	public List<TreeNode> getContentCategoryListByParentId(Long parentId);
	
	/**
	 * 添加节点
	 * @param category
	 * @return
	 */
	public TaotaoResult saveContentCategory(TbContentCategory category);
}
