package util.ding.openplatform;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import util.ding.http.HttpUtil;

/**
 * @author zyf
 * @date 2017/7/26
 */
public class CommonInterface {
    private static Logger logger=Logger.getLogger(CommonInterface.class);

    //获取接口授权
    public static String getAccessToken() {
        String uri = "https://oapi.dingtalk.com/gettoken?corpid=" + CropConfig.CorpId + "&corpsecret=" +  CropConfig.CorpSecret;
        String accessToken = null;
        try {
            //执行请求
            String rs = HttpUtil.sendHttpGet(uri);
            JSONObject json = JSONObject.parseObject(rs);
            Integer errCode = json.getInteger("errcode");
            if (0 == errCode) {
                accessToken = json.getString("access_token");
            } else {
                logger.error("钉钉获取token失败，原因:"+json.getString("errmsg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }
}
