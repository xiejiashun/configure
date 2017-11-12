package com.taotao.search.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.nntp.NewGroupsOrNewsQuery;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.dao.SearchItemDao;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemMapper mapper;

	@Autowired
	private SolrServer solrServer;

	@Autowired
	private SearchItemDao searchdao;

	@Override
	public TaotaoResult importAllToIndex() throws Exception {
		// 1.注入searchitemMapper
		// 2.调用mapepr的方法获取商品列表
		List<SearchItem> itemList = mapper.getSearchItemList();
		// 3.使用solrj来实现商品的导入到索引库中
		// 3.1 创建solrserver对象 由spring管理

		List<SolrInputDocument> documents = new ArrayList<>();
		for (SearchItem searchItem : itemList) {
			// 3.2 遍历商品的列表 将商品的对象一个个 放到solrinpudocument中 （一个POJO 对应document）
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", searchItem.getId().toString());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			documents.add(document);
		}
		// 3.3 将文档对象添加到 索引库中
		solrServer.add(documents);
		// 3.4 提交
		solrServer.commit();

		return TaotaoResult.ok();
	}

	@Override
	public SearchResult search(String queryString, Integer page, Integer rows) throws Exception {
		// 构建solrquery对象 设置各种各样的条件
		SolrQuery query = new SolrQuery();
		query.setQuery(queryString);
		// 设置分页
		if (page == null)
			page = 1;
		if (rows == null)
			rows = 60;
		query.setStart((page - 1) * rows);// page-1 * rows
		query.setRows(rows);
		// 设置默认的搜索域
		query.set("df", "item_keywords");

		// 开启高亮 设置高亮显示的域 设置高亮的前缀和后缀
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		// 1.注入dao
		// 2.调用dao的方法 solrquery 返回商品的列表 和总记录数 （还需要设置总页数 ）
		SearchResult result = searchdao.search(query);

		// 3.补充其他的属性

		long pageCount = result.getRecordCount() / rows;
		if (result.getRecordCount() % rows > 0) {
			pageCount++;
		}
		result.setPageCount(pageCount);
		// 4.返回
		return result;
	}

	// 调用dao的方法获取商品的数据 更新索引库
	@Override
	public TaotaoResult updateSearchItemIndex(Long itemId)  throws Exception{
		// 1.注入dao 查询商品的数据
		SearchItem searchItem = mapper.getSearchItemById(itemId);
		// 2.通过solrj 实现商品的更新
		// 获取连接solrserver 由spring管理 使用

		// 创建solrinputdocument
		SolrInputDocument document = new SolrInputDocument();
		// 添加域
			document.addField("id", searchItem.getId().toString());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());

		// 添加文档到索引库中
		
		solrServer.add(document);

		// 提交
		solrServer.commit();
		return TaotaoResult.ok();
	}

}
