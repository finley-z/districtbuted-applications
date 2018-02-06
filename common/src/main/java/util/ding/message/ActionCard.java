package util.ding.message;

/**
 * @author zyf
 * @date 2017/8/24
 */
public class ActionCard{
    private String title;
    private String markdown;
    private String single_title;
    private String single_url;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String getSingle_title() {
        return single_title;
    }

    public void setSingle_title(String single_title) {
        this.single_title = single_title;
    }

    public String getSingle_url() {
        return single_url;
    }

    public void setSingle_url(String single_url) {
        this.single_url = single_url;
    }
}
