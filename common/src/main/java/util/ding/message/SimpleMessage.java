package util.ding.message;

/**
 * @author zyf
 * @date 2017/8/24
 */
public class SimpleMessage extends Message{
    private Text text;

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
