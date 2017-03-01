import com.tyaer.net.bean.ResponseBean;
import com.tyaer.net.http.HttpHelper;
import org.apache.http.HttpHost;

/**
 * Created by Twin on 2017/1/18.
 */
public class Test {
    public static void main(String[] args) {
        HttpHost httpHost = new HttpHost("svnserver", 808);
        ResponseBean responseBean = HttpHelper.sendRequest("https://twitter.com/LiveShanghai",httpHost);
        System.out.println(responseBean);
    }

    @org.junit.Test
    public void request(){
        String url = "http://i.maxthon.cn/";
        ResponseBean responseBean = HttpHelper.sendRequest(url);
        System.out.println(responseBean);
    }
}
