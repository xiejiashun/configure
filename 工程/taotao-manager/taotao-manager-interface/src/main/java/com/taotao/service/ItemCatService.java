package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.TreeNode;

public interface ItemCatService {
	/**
	 * 根据父节点的id查询该节点下的所有的子节点列表
	 * @param parentId
	 * @return
	 */
	public List<TreeNode> getItemCatListByParentId(Long parentId);
}
