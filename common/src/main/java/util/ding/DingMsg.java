package util.ding;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import util.ding.http.HttpUtil;
import util.ding.message.Message;
import util.ding.openplatform.ChatGroup;
import util.ding.openplatform.CommonInterface;


/**
 * @author zyf
 * @date 2017/7/26
 */
public class DingMsg {
    private static Logger logs = Logger.getLogger(DingMsg.class);

    //创建钉钉会话群
    public static void createChatGroup(ChatGroup chatGroup) {
        String uri = "https://oapi.dingtalk.com/chat/create?access_token=" + CommonInterface.getAccessToken();
        try {
            String params = JSON.toJSONString(chatGroup);
//          String params="{\"name\":\"钉钉消息开发测试\",\"owner\":\"01025232558743\",\"useridlist\":[\"01025232558743\",\"35624006945008\"]}";
            String rs = HttpUtil.sendHttpPost(uri, params);
            logs.info("创建钉钉群组结果:" + rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送钉钉消息至会话群
    public static String sendMsgToChatGroup(Message message) {
        String uri = "https://oapi.dingtalk.com/chat/send?access_token=" + CommonInterface.getAccessToken();
        try {
            String params = JSON.toJSONString(message);
            String rs = HttpUtil.sendHttpPost(uri, params);
            logs.info("钉钉群消息发送结果:" + rs);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    //发送短消息至个人用户，或者会话群
    public static String sendOrdinaryMsg(Message message) {
        String uri = "https://oapi.dingtalk.com/message/send_to_conversation?access_token=" + CommonInterface.getAccessToken();
        try {
            String params = JSON.toJSONString(message);
            String rs = HttpUtil.sendHttpPost(uri, params);
            logs.info("钉钉普通消息发送结果:" + rs);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
