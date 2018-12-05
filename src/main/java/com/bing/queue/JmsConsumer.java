package com.bing.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author bing
 *
 */
public class JmsConsumer {
	//默认连接用户名
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	//默认连接密码
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	//默认连接地址
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;
	//发送的消息数量
	private static final int SENDNUM = 10;
	
	public static void main(String[] args) {
		ConnectionFactory connectionFactory;
		Connection connection = null;
		Session session = null;
		Destination destination;
		MessageConsumer messageConsumer = null;
		
		//1.创建连接工厂
		connectionFactory = new ActiveMQConnectionFactory(JmsConsumer.USERNAME,JmsConsumer.PASSWORD,JmsConsumer.BROKEURL);
		try {
			//2.获取连接
			connection = connectionFactory.createConnection();
			//3.启动连接
			connection.start();
			//4.获取session(参数1：是否启动事务,参数2：消息确认模式)
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			//5.创建队列对象，创建一个连接生产者队列（HelloAM）的消息队列（如果activemq上有这样一个队列，则监听该队列，如果没有，则创建）
			destination = session.createQueue("HelloAM");
			//6.创建消息消费
			messageConsumer = session.createConsumer(destination);
			//7.读取消息
			while (true) {
				TextMessage textMessage = (TextMessage)messageConsumer.receive(5000);  //receive方法是个同步的方法
				if(textMessage != null) {
					System.out.println(textMessage.getText());
				} else {
					break;
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			//8.关闭资源
			try {
				if(messageConsumer != null) {
					messageConsumer.close();
				}
				if(session != null) {
					session.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
