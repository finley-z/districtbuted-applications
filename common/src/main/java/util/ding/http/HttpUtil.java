package util.ding.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

/**
 * @author zyf
 * @date 2017/7/26
 */
public class HttpUtil {

    static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
    static HttpClient httpClient = new HttpClient(connectionManager);

    static {
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20 * 1000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(20 * 1000);
    }

    /**
     * 发送Get请求
     *
     * @param url
     * @return
     */
    public static String sendHttpGet(String url) {
        GetMethod getMethod = new GetMethod(url);
        String rs = null;
        try {
            int st = httpClient.executeMethod(getMethod);
            rs = getMethod.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
        return rs;
    }


    /**
     * 发送post请求
     *
     * @param url
     * @param params
     * @return
     */
    public static String sendHttpPost(String url, String params) {
        PostMethod postMethod = new PostMethod(url);
        String rs = null;
        try {
            postMethod.setRequestEntity(new ByteArrayRequestEntity(params.getBytes("UTF-8")));
            postMethod.setRequestHeader("content-type", "application/json;charset=UTF-8");
            int st = httpClient.executeMethod(postMethod);
            rs = postMethod.getResponseBodyAsString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return rs;
    }
}
