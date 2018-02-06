package middleware.mq;

import javax.jms.*;

/**
 * @author zyf
 * @date 2018/2/2
 */
public class ActiveMQReceiver {
    public static void receiver(){
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // 消费者，消息接收者
        MessageConsumer consumer;
        try {
            //这个最好还是有事务
            session = ActiveMQInstance.getSession();
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createQueue("test");
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        String msg  =((TextMessage) message).getText();
                        if (null != message) {
                            System.out.println("收到消息" + msg);
                        }
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void main(String []args){
        receiver();
    }
}
