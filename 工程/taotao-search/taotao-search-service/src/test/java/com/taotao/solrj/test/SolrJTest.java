package com.taotao.solrj.test;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrJTest {
	//添加索引
	@Test
	public void testAdd() throws Exception{
		//1.创建连接对象 solrserver对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.154:8080/solr");
		//2.创建文档对象
		SolrInputDocument document = new SolrInputDocument();
		//3.向文档对象添加域
		document.addField("id", "test00011");
		document.addField("item_title", "测试标题");
		//4.添加文档到索引库中
		solrServer.add(document);
		//5.提交
		solrServer.commit();
	}
	//查询
	@Test
	public void testQuery() throws Exception{
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.154:8080/solr");
		//2.创建查询的对象
		SolrQuery query = new SolrQuery();
		//3.设置查询的条件  
		query.setQuery("*:*");
		//。。。。
		//。。。。。。。。。。。。
		//4.执行查询
		QueryResponse response = solrServer.query(query);
		//5.获取结果集
		SolrDocumentList documentList = response.getResults();
		System.out.println("查询命中："+documentList.getNumFound());
		//6.遍历结果集
		for (SolrDocument solrDocument : documentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
		}
		//7.打印--》处理数据导入到索引库。。。。。中
	}
	
	//测试solrj操作solr集群
	//添加
	@Test
	public void testSolrjCloud() throws Exception{
		//1.创建solrserver对象（连接到solr集群中） cloudsolrserver对象
		//指定zookeeper的集群的地址
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.154:2181,192.168.25.154:2182,192.168.25.154:2183");
		//2.设置默认的collection  只需要指定 逻辑上的索引集合
		solrServer.setDefaultCollection("collection2");
		//3.创建文档对象
		SolrInputDocument document = new SolrInputDocument();
		//4.添加域
		document.addField("id", "test001");
		document.addField("item_title", "测试标题");
		//5.向索引库中添加文档
		
		solrServer.add(document);
		
		//6.提交
		solrServer.commit();
		
	}
	
	//test删除
	
	@Test
	public void testDelete() throws Exception{
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.154:2181,192.168.25.154:2182,192.168.25.154:2183");
		//2.设置默认的collection  只需要指定 逻辑上的索引集合
		solrServer.setDefaultCollection("collection2");
		solrServer.deleteByQuery("*:*");
		solrServer.commit();
	}
	
	
	
}
