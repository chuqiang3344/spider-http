package wx;

import com.tyaer.net.utils.HttpUtils;
import org.junit.Test;

/**
 * Created by Twin on 2017/1/10.
 */
public class Cookie_Test {

    @Test
    public void getWebCookie_Test(){
        System.out.println(System.currentTimeMillis());
        String url="http://weixin.sogou.com/";
        String cookie1 = HttpUtils.getWebCookie(url, "");
        System.out.println(cookie1);
        String cookie2 = HttpUtils.getWebCookie("http://pb.sogou.com/pv.gif?uigs_t=1483950070110&uigs_productid=weixin&type=weixin_search_pc&pagetype=index&wuid=undefined&uigs_uuid=1483950069921410&login=0&uigs_refer=&", cookie1);
        System.out.println(cookie2);
    }
}
