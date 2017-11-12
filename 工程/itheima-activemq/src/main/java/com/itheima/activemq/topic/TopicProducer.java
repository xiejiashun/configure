package com.itheima.activemq.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TopicProducer {

	// 发送topic
	@Test
	public void send() throws Exception {
		//1.创建连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		//2.创建连接
		Connection connection = factory.createConnection();
		//3.开启连接
		connection.start();
		//4.创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.创建目的地    topic
		Topic createTopic = session.createTopic("topic-test");
		//6.创建生成者
		MessageProducer producer = session.createProducer(createTopic);
		//7.构建消息对象
		TextMessage createTextMessage = session.createTextMessage("topic发送的消息123");
		//8.发送消息
		producer.send(createTextMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
}
