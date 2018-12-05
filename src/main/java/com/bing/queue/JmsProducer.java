package com.bing.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author bing
 *
 */
public class JmsProducer {
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
		MessageProducer messageProducer = null;
		
		//1.创建连接工厂
		connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEURL);
		try {
			//2.获取连接
			connection = connectionFactory.createConnection();
			//3.启动连接
			connection.start();
			/*4.获取session  (参数1：是否启动事务,参数2：消息确认模式)
			 * createSession
			 * 两个参数：
			 * 1.事务（当前会话是否启用事务，如果消息发送者向消息代理发送消息的时候需要消息代理确认，如果消息代理没确认，那么消息发送及后续动作都要回滚）
			 * 2.消息的确认模式(消息接收者在每次接收消息的时候要向消息发送者发送确认)
			 * AUTO_ACKNOWLEDGE 自动签收
			 * CLIENT_ACKNOWLEDGE 客户端自行调用acknowledge方法签收
			 * DUPS_OK_ACKNOWLEDGE 不是必须签收，消息可能重复发送，在第二次发送的时候可能有个头部标志标识未已经发送过一次，客户端在接收时要对消息的重复自行做一个控制
			 * */
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			//5.创建队列对象
			destination = session.createQueue("HelloAM");
			//6.创建消息生产者
			messageProducer = session.createProducer(destination);
			for (int i = 0; i < SENDNUM; i++) {
				String msg = "发送消息" + i + "-" + System.currentTimeMillis();
				//7.创建消息
				TextMessage message = session.createTextMessage(msg);
				System.out.println("发送消息：" + msg);
				//8.发送消息
				messageProducer.send(message);
			}
			session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			//9.关闭资源
			try {
				if(messageProducer != null) {
					messageProducer.close();
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
