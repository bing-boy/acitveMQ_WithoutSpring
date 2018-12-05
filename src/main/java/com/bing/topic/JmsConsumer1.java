package com.bing.topic;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author bing
 *
 */
public class JmsConsumer1 {
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
		Topic topic;
		MessageConsumer messageConsumer = null;
		
		//1.创建连接工厂
		connectionFactory = new ActiveMQConnectionFactory(JmsConsumer1.USERNAME,JmsConsumer1.PASSWORD,JmsConsumer1.BROKEURL);
		try {
			//2.获取连接
			connection = connectionFactory.createConnection();
			//3.启动连接
			connection.start();
			//4.获取session(参数1：是否启动事务,参数2：消息确认模式)
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			//5.创建主题对象
			topic = session.createTopic("topic");
			//6.创建消息消费
			messageConsumer = session.createConsumer(topic);
			//7.监听消息
			messageConsumer.setMessageListener(new MessageListener() {
	            public void onMessage(Message message) {
	                TextMessage textMessage = (TextMessage) message;
	                try {
	                    System.out.println("接收到消息："+textMessage.getText());
	                } catch (JMSException e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	        //8.等待键盘输入,这里的作用其实就是为了让上面的监听消息一直进行，直到控制台输入了消息回车后才停止
			System.in.read();
		} catch (JMSException  e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//9.关闭资源
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
