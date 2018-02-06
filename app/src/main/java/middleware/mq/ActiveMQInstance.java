package middleware.mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author zyf
 * @date 2018/2/2
 */
public class ActiveMQInstance {
    public static Session getSession() {

        //ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;

        //Connection ：JMS 客户端到JMS
        Connection connection = null;

        //Session： 一个发送或接收消息的线程
        Session session = null;

        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD, "tcp://192.168.1.178:61616");

        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            connection.start();

            // 获取操作连接
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return session;
    }
}
