package com.itheima.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class QueueProducer {
	//生产者发送消息
	@Test
	public void send() throws Exception{
		//1.创建连接服务器的工厂
		//参数：就是连接服务器的地址和端口 使用的是TCP的协议
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		//2.获取连接对象
		Connection connection = factory.createConnection();
		//3.开启连接
		connection.start();
		
		//4.创建session   提供了很多发送 创建的方法
		//参数1：表示是否开启 分布式事务  不开启。（JTA）
		//参数2：表示消息的应答模式（手动和自动） 一般是使用自动应答  只有  第一个参数为false 的时候，参数2 设置为自动应答才有意义。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//5.指定发送消息的目的地
		
		Queue queue = session.createQueue("test-queue");
		
		//6.构建消息的内容
		
		TextMessage message = session.createTextMessage("这是一个测试");
		
		
		//7.创建生产者 
		MessageProducer producer = session.createProducer(queue);
		
		
		//8.发送消息
		
		producer.send(message);
		
		
		//9.关闭资源
		
		producer.close();
		
		session.close();
		
		connection.close();
	}
}
