package util.ding;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.ding.message.SimpleMessage;
import util.ding.message.Text;


/**
 * @author zyf
 * @date 2017/7/27
 */
public class DingWarning {

    public static String sendWarningMsg(String chatId, String content) {
        String resultCode = "-1";
        try {
            SimpleMessage message = new SimpleMessage();
            message.setChatid(chatId);
            message.setMsgtype("text");
            Text text=new Text();
            text.setContent(content);
            message.setText(text);
//            ActionCard actionCard = new ActionCard();
//            actionCard.setTitle("预警提醒");
//            actionCard.setMarkdown(content);
//            actionCard.setSingle_title("预警提醒");
//            actionCard.setSingle_url("https://open.dingtalk.com");
//            message.setAction_card(actionCard);
            String res = DingMsg.sendMsgToChatGroup(message);
            JSONObject resObj = JSON.parseObject(res);
            if (resObj != null) {
                resultCode = resObj.getString("errcode");
                if ("0".equals(resultCode)) {
                    resultCode = "100";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultCode = "-1";
        }
        return resultCode;
    }
}
