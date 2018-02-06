package util.ding.openplatform;

import java.util.List;

/**
 * @author zyf
 * @date 2017/7/27
 */
public class ChatGroup {
    private String name;
    private String owner;
    private List<String>  useridlist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getUseridlist() {
        return useridlist;
    }

    public void setUseridlist(List<String> useridlist) {
        this.useridlist = useridlist;
    }
}
