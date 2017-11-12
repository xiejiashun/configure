package com.itheima.httpclient;

import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DoGETParam {

	public static void main(String[] args) throws Exception {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		// 定义请求的参数
//		 new URIBuilder("http://localhost:8081/item/list").addParameter("a", "1").addParameter("a", "2");
//		 new URIBuilder("http://localhost:8081/item/list").setParameter("a", "1").setParameter("a", "2");
		 //http://localhost:8081/item/list?a=1&a=2
		 //http://localhost:8081/item/list?a=2
		URI uri = new URIBuilder("http://localhost:8081/item/list").setParameter("page", "1").setParameter("rows", "5")
				.build();

		System.out.println(uri);

		// 创建http GET请求
		HttpGet httpGet = new HttpGet(uri);

		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				String content = EntityUtils.toString(response.getEntity(), "UTF-8");
				System.out.println(content);
			}
		} finally {
			if (response != null) {
				response.close();
			}
			httpclient.close();
		}

	}

}
