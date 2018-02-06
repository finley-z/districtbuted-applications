package middleware.mq;

import javax.jms.*;

/**
 * @author zyf
 * @date 2018/2/2
 */
public class ActiveMQSender {

    public static void sendMessage(String queue,String msg) throws JMSException {
        // Session： 一个发送或接收消息的线程
        Session session = ActiveMQInstance.getSession();

        // TextMessage message;
        MessageProducer producer = null;

        // Destination ：消息的目的地;消息发送给谁.
        Queue destination;
        try {
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createQueue(queue);
            // 得到消息生成者
            producer = session.createProducer(destination);
            // 设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            TextMessage message = session.createTextMessage(msg);
            producer.send(message);
            producer.close();
            session.commit();

        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public static void main(String [] args)
    {
        try {
            sendMessage("123","2354252");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
