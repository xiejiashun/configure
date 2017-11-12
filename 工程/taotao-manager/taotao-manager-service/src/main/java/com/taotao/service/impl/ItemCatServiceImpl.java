package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private TbItemCatMapper mapper;

	@Override
	public List<TreeNode> getItemCatListByParentId(Long parentId) {
		//注入mapper
		//创建example对像
		TbItemCatExample example = new TbItemCatExample();
		//设置查询的的条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);//select * from tbitemcat where parentId=1
		
		//调用方法查询得到结果集    List<tbitemcat>
		List<TbItemCat> list = mapper.selectByExample(example);
		
		
		//转成list<TreeNode>
		List<TreeNode> nodes = new ArrayList<>();
		
		for (TbItemCat itemCat : list) {
			TreeNode node = new TreeNode();
			node.setId(itemCat.getId());
			node.setText(itemCat.getName());
			node.setState(itemCat.getIsParent()?"closed":"open");//closed  open
			nodes.add(node);
		}
		return nodes;
	}

}
