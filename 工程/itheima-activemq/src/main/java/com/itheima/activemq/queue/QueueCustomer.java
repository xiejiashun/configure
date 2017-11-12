package com.itheima.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class QueueCustomer {
	@Test
	public void recieve() throws Exception {
		//1.创建连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
		//2.建立连接
		Connection connection = factory.createConnection();
		
		//3.开启连接
		connection.start();
		//4.创建session 
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//5.创建目的地 （要从哪一个地方取消息）
		
		Queue queue = session.createQueue("test-queue");
		//6.创建一个消费者 
		MessageConsumer consumer = session.createConsumer(queue);
		//7.接收消息
		//有两种接收消息的方式  
		//1.直接接受   测试时使用。
//		while(true){
//			//接收消息 如果超过 10秒钟就断开连接  参数是超时时间：毫秒
//			Message receive = consumer.receive(10000);
//			
//			if(receive==null){
//				break;
//			}
//			
//			if(receive instanceof TextMessage){
//				TextMessage message= (TextMessage)receive;
//				
//				System.out.println("接收到的消息的内容是："+message.getText());
//			}
//		}
		
		//2.设置一个消息的监听器 监听消息      实际上是开启了一个新的线程.  生产环境 使用这种方式
		System.out.println("start");
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
					try {
						if(message instanceof TextMessage){
							TextMessage message2 =(TextMessage)message;
							System.out.println("监听器接收到的消息》》》"+message2.getText());
						}
					} catch (JMSException e) {
						e.printStackTrace();
					}
			}
		});
		System.out.println("end");
		Thread.sleep(9999999);
		//8.关闭资源
		
		consumer.close();
		session.close();
		connection.close();
		
	}
}
