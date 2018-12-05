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
	//Ĭ�������û���
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	//Ĭ����������
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	//Ĭ�����ӵ�ַ
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;
	//���͵���Ϣ����
	private static final int SENDNUM = 10;
	
	public static void main(String[] args) {
		ConnectionFactory connectionFactory;
		Connection connection = null;
		Session session = null;
		Topic topic;
		MessageConsumer messageConsumer = null;
		
		//1.�������ӹ���
		connectionFactory = new ActiveMQConnectionFactory(JmsConsumer1.USERNAME,JmsConsumer1.PASSWORD,JmsConsumer1.BROKEURL);
		try {
			//2.��ȡ����
			connection = connectionFactory.createConnection();
			//3.��������
			connection.start();
			//4.��ȡsession(����1���Ƿ���������,����2����Ϣȷ��ģʽ)
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			//5.�����������
			topic = session.createTopic("topic");
			//6.������Ϣ����
			messageConsumer = session.createConsumer(topic);
			//7.������Ϣ
			messageConsumer.setMessageListener(new MessageListener() {
	            public void onMessage(Message message) {
	                TextMessage textMessage = (TextMessage) message;
	                try {
	                    System.out.println("���յ���Ϣ��"+textMessage.getText());
	                } catch (JMSException e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	        //8.�ȴ���������,�����������ʵ����Ϊ��������ļ�����Ϣһֱ���У�ֱ������̨��������Ϣ�س����ֹͣ
			System.in.read();
		} catch (JMSException  e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//9.�ر���Դ
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
