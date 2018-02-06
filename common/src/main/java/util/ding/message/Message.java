package util.ding.message;

/**
 * @author zyf
 * @date 2017/7/27
 */
public class Message {
    protected String chatid;
    protected String msgtype;

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
}
