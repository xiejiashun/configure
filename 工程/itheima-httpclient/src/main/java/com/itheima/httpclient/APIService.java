package com.itheima.httpclient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class APIService {

	// 带参数的get请求
	public HttpResult doGet(String url, Map<String, Object> map) throws Exception {
		// 1.创建httpclient对象（打开浏览器）
		CloseableHttpClient httpClient = HttpClients.createDefault();
		URIBuilder uriBuilder = new URIBuilder(url);
		// 3.设置参数
		if (map != null) {
			Set<Entry<String, Object>> entrySet = map.entrySet();

			for (Entry<String, Object> entry : entrySet) {
				uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}

		URI uri = uriBuilder.build();
		// 2.创建httpget请求(输入地址)
		HttpGet get = new HttpGet(uri);

		// 4.执行请求（按回车发送请求）
		CloseableHttpResponse response = httpClient.execute(get);

		// 5.获取响应内容（包括响应的状态码 还有响应的内容）
		int code = response.getStatusLine().getStatusCode();
		// 判断response.getEntity() 是否为空，如果不为空获取值，如果为空设置为空
		String body = null;
		if (response.getEntity() != null) {
			body = EntityUtils.toString(response.getEntity(), "utf-8");
		}
		// 6.封装到httpresult中 返回
		return new HttpResult(code, body);
	}

	// 不带参数的get请求
	public HttpResult doGet(String url) throws Exception {
		return doGet(url, null);
	}

	// 带参数的post请求

	public HttpResult doPost(String url, Map<String, Object> map) throws Exception {

		// 1.创建httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		// 2.创建httppost请求
		HttpPost post = new HttpPost(url);
		// 参数有值
		if (map != null) {

			List<NameValuePair> parameters = new ArrayList<>();// 表单的参数列表

			Set<Entry<String, Object>> entrySet = map.entrySet();

			for (Entry<String, Object> entry : entrySet) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
			// 3.为post请求对象设置表单实体
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
			post.setEntity(entity);
		}
		// 4.执行请求
		CloseableHttpResponse response = httpClient.execute(post);

		// 5.获取响应内容（包括响应的状态码 还有响应的内容）
		int code = response.getStatusLine().getStatusCode();
		// 判断response.getEntity() 是否为空，如果不为空获取值，如果为空设置为空
		String body = null;
		if (response.getEntity() != null) {
			body = EntityUtils.toString(response.getEntity(), "utf-8");
		}
		// 6.封装到httpresult中 返回
		return new HttpResult(code, body);

	}

	// 不带参数的post请求
	public HttpResult doPost(String url) throws Exception {
		return doPost(url,null);
	}
	

	// put delete
}
