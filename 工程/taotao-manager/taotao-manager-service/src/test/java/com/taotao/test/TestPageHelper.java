package com.taotao.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;

public class TestPageHelper {
	@Test
	public void testPagehelper(){
		//1.初始化spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//2.获取mapper的代理对象
		TbItemMapper itemMapper = context.getBean(TbItemMapper.class);
		//设置分页
		PageHelper.startPage(1, 10);
		TbItemExample example  = new TbItemExample();//设置条件的对象   select * from tbitem;
		
//		Criteria criteria = example.createCriteria();//设置条件
//		
//		//根据商品的名称查询商品
//		criteria.andTitleEqualTo("商品测试");//select * from tbitem where title="商品测试"
//		criteria.andNumEqualTo(123);///select *　 from tbitem where title="商品测试" and num=123
		
		//3.mapper的方法查询列表
		List<TbItem> list = itemMapper.selectByExample(example);//参数exmaple是各种条件设置的对象
		
		List<TbItem> list2 = itemMapper.selectByExample(example);//参数exmaple是各种条件设置的对象
		
		System.out.println(list.size()+"第二个："+list2.size());
		
		//4.获取分页的信息
		
		PageInfo<TbItem> info = new PageInfo<>(list);
		
		System.out.println("获取总记录数据"+info.getTotal());
		
		System.out.println(info.getList());//分页的数据列表
		
		
	}
}
