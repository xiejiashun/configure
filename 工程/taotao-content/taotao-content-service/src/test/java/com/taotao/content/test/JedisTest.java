package com.taotao.content.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	//单机版
	@Test
	public void testSet(){
		//1.创建连接的对象jedis 
		Jedis jedis = new Jedis("192.168.25.153", 6379);
		//2.直接set
		jedis.set("keysingle", "ceshi");
		//3.获取值 打印
		System.out.println(jedis.get("keysingle"));
		//4.关闭连接对象
		jedis.close();
	}
	
	//使用连接池
	@Test
	public void testPoolSet(){
		//1.创建连接的对象jedispool 
		JedisPool pool = new JedisPool("192.168.25.153", 6379);
		//2.获取jedis对象
		Jedis resource = pool.getResource();
		//3.设置值
		resource.set("keyspool", "ceshi");
		//3.获取值 打印
		System.out.println(resource.get("keyspool"));
		
		//4.获取值
		
		//5.关闭jedis对象（释放资源）
		resource.close();
		
		
		//6.关闭连接池 （一般在系统关闭的时候）
		
		pool.close();
		
	}
	
	
	//集群版
	@Test
	public void testjedistCluster(){
		//1.创建jedisCluster对象 指定连接的地址 和端口（6个）
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.153", 7001));
		nodes.add(new HostAndPort("192.168.25.153", 7002));
		nodes.add(new HostAndPort("192.168.25.153", 7003));
		nodes.add(new HostAndPort("192.168.25.153", 7004));
		nodes.add(new HostAndPort("192.168.25.153", 7005));
		nodes.add(new HostAndPort("192.168.25.153", 7006));
		JedisCluster cluster = new JedisCluster(nodes );
		//2.设置值
		cluster.set("clusterkey", "123");
		//3.获取值
		System.out.println(cluster.get("clusterkey"));
		//4.关闭集群连接对象（是在系统关闭的时候关闭）
		cluster.close();
	}
	
	
}
