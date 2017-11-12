package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TreeNode;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper mapper;
	
	@Override
	public List<TreeNode> getContentCategoryListByParentId(Long parentId) {
		//1.注入mapper
		//2.创建exmaple 
		TbContentCategoryExample example = new TbContentCategoryExample();
		//3.设置查询的条件
		example.createCriteria().andParentIdEqualTo(parentId);
		//4.执行查询  得到结果    list<tbContentCategory>
		List<TbContentCategory> list = mapper.selectByExample(example);
		List<TreeNode> nodes = new ArrayList<>();
		//5.转换List<TreeNode
		for (TbContentCategory tbContentCategory : list) {
			TreeNode node = new TreeNode();
			node.setId(tbContentCategory.getId());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			node.setText(tbContentCategory.getName());
			nodes.add(node);
		}
		//6.返回
		return nodes;
	}

	@Override
	public TaotaoResult saveContentCategory(TbContentCategory category) {
		//1.注入mapper
		//2.补全其他的属性
		category.setCreated(new Date());
		category.setIsParent(false);
		category.setSortOrder(1);
		category.setStatus(1);//正常
		category.setUpdated(category.getCreated());
		
		//判断  添加的节点的父节点 本身是否为叶子节点 如果是：更新成父节点 如果不是，不管了
		//查询父节点对象     
		TbContentCategory parent = mapper.selectByPrimaryKey(category.getParentId());
		if(parent.getIsParent()==false){//本身是叶子节点
			parent.setIsParent(true);
			//更新到数据库
			mapper.updateByPrimaryKey(parent);
		}
		
		//3.插入数据库中
		mapper.insertSelective(category);
		return TaotaoResult.ok(category);
	}

}
