package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDatagridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper mapper;
	
	@Autowired
	private JedisClient client;//get set 方法 hget
	
	@Override
	public EasyUIDatagridResult getContentListBy(Long categoryId, Integer page, Integer rows) {
		//分页查询分类下的内容列表
		//1.注入mapper
		//2.设置分页
		PageHelper.startPage(page, rows);
		//3.创建exmaple对象
		TbContentExample example = new TbContentExample();
		//4.设置查询的条件     
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		//5.执行查询   list<tbcontent>--->分页后的
		List<TbContent> list = mapper.selectByExample(example);
		//6.获取分页的信息
		PageInfo<TbContent> info = new PageInfo<>(list);
		
		//7.设置EasyUIDatagridResult 对象封装属性
		EasyUIDatagridResult result = new EasyUIDatagridResult();
		result.setTotal(info.getTotal());
		result.setRows(info.getList());
		return result;
	}

	
	
	@Override
	public TaotaoResult saveContent(TbContent content) {
		//注入maper
		//补全属性
		//删除key 
		
		try {
			client.hdel("CONTENT_REDIS_KEY", content.getCategoryId()+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		content.setCreated(new Date());
		content.setUpdated(content.getCreated());
		//调用方法
		mapper.insertSelective(content);
		
		return TaotaoResult.ok();
	}



	@Override
	public List<TbContent> getContentListByCategoryId(Long categoryId) {
		//添加缓存不能影响正常的业务逻辑
		
		//判断  如果有缓存 直接返回（查询缓存）
		//1.根据key获取redis中的数据
		try {
			String stringjson = client.hget("CONTENT_REDIS_KEY", categoryId+"");
			//如果有 直接返回  ，如果没有 查询数据库 
			if(StringUtils.isNotBlank(stringjson)){
				System.out.println("有缓存。。。。。。。。。。。。。");
				return JsonUtils.jsonToList(stringjson, TbContent.class);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//创建tbcontentExample
		TbContentExample example = new TbContentExample();
		//设置查询的条件
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		//执行查询 返回list<TbContent>
		List<TbContent> list = mapper.selectByExample(example);
		
		
		//从数据库查询  写入redis (添加缓存)  
		//注入jedisclient 
		//直接set 
		try {
			System.out.println("没有缓存..................");
			client.hset("CONTENT_REDIS_KEY", categoryId+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		return list;
	}

}
