package proxy;

import com.tyaer.net.bean.ResponseBean;
import com.tyaer.net.http.HttpHelper;
import org.apache.http.HttpHost;

/**
 * Created by Twin on 2017/2/22.
 */
public class Proxy_Test {
    public static void main(String[] args) {
        HttpHost httpHost = new HttpHost("39.86.60.118", 9999);
        ResponseBean responseBean = HttpHelper.sendRequest("http://www.secretchina.com/",httpHost);
        System.out.println(responseBean);
    }
}
