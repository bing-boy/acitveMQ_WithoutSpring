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
		Destination destination;
		MessageProducer messageProducer = null;
		
		//1.�������ӹ���
		connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEURL);
		try {
			//2.��ȡ����
			connection = connectionFactory.createConnection();
			//3.��������
			connection.start();
			/*4.��ȡsession  (����1���Ƿ���������,����2����Ϣȷ��ģʽ)
			 * createSession
			 * ����������
			 * 1.���񣨵�ǰ�Ự�Ƿ��������������Ϣ����������Ϣ��������Ϣ��ʱ����Ҫ��Ϣ����ȷ�ϣ������Ϣ����ûȷ�ϣ���ô��Ϣ���ͼ�����������Ҫ�ع���
			 * 2.��Ϣ��ȷ��ģʽ(��Ϣ��������ÿ�ν�����Ϣ��ʱ��Ҫ����Ϣ�����߷���ȷ��)
			 * AUTO_ACKNOWLEDGE �Զ�ǩ��
			 * CLIENT_ACKNOWLEDGE �ͻ������е���acknowledge����ǩ��
			 * DUPS_OK_ACKNOWLEDGE ���Ǳ���ǩ�գ���Ϣ�����ظ����ͣ��ڵڶ��η��͵�ʱ������и�ͷ����־��ʶδ�Ѿ����͹�һ�Σ��ͻ����ڽ���ʱҪ����Ϣ���ظ�������һ������
			 * */
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			//5.�������ж���
			destination = session.createQueue("HelloAM");
			//6.������Ϣ������
			messageProducer = session.createProducer(destination);
			for (int i = 0; i < SENDNUM; i++) {
				String msg = "������Ϣ" + i + "-" + System.currentTimeMillis();
				//7.������Ϣ
				TextMessage message = session.createTextMessage(msg);
				System.out.println("������Ϣ��" + msg);
				//8.������Ϣ
				messageProducer.send(message);
			}
			session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			//9.�ر���Դ
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
