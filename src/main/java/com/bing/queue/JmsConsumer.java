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
		MessageConsumer messageConsumer = null;
		
		//1.�������ӹ���
		connectionFactory = new ActiveMQConnectionFactory(JmsConsumer.USERNAME,JmsConsumer.PASSWORD,JmsConsumer.BROKEURL);
		try {
			//2.��ȡ����
			connection = connectionFactory.createConnection();
			//3.��������
			connection.start();
			//4.��ȡsession(����1���Ƿ���������,����2����Ϣȷ��ģʽ)
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			//5.�������ж��󣬴���һ�����������߶��У�HelloAM������Ϣ���У����activemq��������һ�����У�������ö��У����û�У��򴴽���
			destination = session.createQueue("HelloAM");
			//6.������Ϣ����
			messageConsumer = session.createConsumer(destination);
			//7.��ȡ��Ϣ
			while (true) {
				TextMessage textMessage = (TextMessage)messageConsumer.receive(5000);  //receive�����Ǹ�ͬ���ķ���
				if(textMessage != null) {
					System.out.println(textMessage.getText());
				} else {
					break;
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			//8.�ر���Դ
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
