package com.taotao.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;

@Repository
public class SearchItemDao {
	// 根据solrquery查询数据

	@Autowired
	private SolrServer solrServer;
	

	public SearchResult search(SolrQuery query) throws Exception {
		// 1.创建连接对象solrserver对象 由spring管理，直接注入
		// 2.执行查询
		QueryResponse response = solrServer.query(query);
		// 取高亮
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

		// 3.获取结果集
		SolrDocumentList results = response.getResults();
		long recordcount = results.getNumFound();// 总记录数
		// 4.遍历结果集
		List<SearchItem> searchitemlist = new ArrayList<>();
		for (SolrDocument solrDocument : results) {// solrDocument=====>searchitem对象
			// 5.将结果集中商品的数据 封装到searchresult中（设置总记录数和商品的列表）
			SearchItem item = new SearchItem();
			item.setCategory_name(solrDocument.get("item_category_name").toString());
			item.setId(Long.valueOf(solrDocument.get("id").toString()));
			item.setImage(solrDocument.get("item_image").toString());
			// item.setItem_desc(item_desc);//页面中不需要展示描述
			item.setPrice((Long) solrDocument.get("item_price"));
			item.setSell_point(solrDocument.get("item_sell_point").toString());
			// 取高亮
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			// 判断是否为空
			String itemtile = "";
			if (list != null && list.size() > 0) {
				// 有高亮
				itemtile = list.get(0);
			} else {
				// 没有高亮
				itemtile = solrDocument.get("item_title").toString();
			}
			item.setTitle(itemtile);
			searchitemlist.add(item);
		}
		// 创建searchresult
		SearchResult result = new SearchResult();
		result.setItemList(searchitemlist);
		result.setRecordCount(recordcount);
		return result;
	}

}
