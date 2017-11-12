package com.taotao.cart.jedis;

import java.util.Map;

public interface JedisClient {

	String set(String key, String value);
	String get(String key);
	Boolean exists(String key);
	Long expire(String key, int seconds);
	Long ttl(String key);
	Long incr(String key);
	Long hset(String key, String field, String value);
	String hget(String key, String field);	
	Long hdel(String key,String... field);//删除hkey
	//获取hash类型的所有的数据
	Map<String, String> hgetAll(String key);
	
}
