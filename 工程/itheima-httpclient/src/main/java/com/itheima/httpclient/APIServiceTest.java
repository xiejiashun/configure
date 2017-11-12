package com.itheima.httpclient;

import java.util.HashMap;
import java.util.Map;

public class APIServiceTest {
	public static void main(String[] args)  throws Exception{
		APIService service = new APIService();
		String url="http://localhost:8081/item/list";
		Map<String, Object> map = new HashMap<>();
		//设置参数
		map.put("page", "1");
		map.put("rows", 10);
		HttpResult result = service.doGet(url,map);
		System.out.println(result.getCode());
		System.out.println(result.getBody());
	}
}
