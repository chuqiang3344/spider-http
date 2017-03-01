package com.tyaer.net.cookie;

import com.tyaer.net.utils.CookieUtils;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Twin on 2017/1/10.
 */
public class Cookie_Test {

    public static void main(String[] args) {
        CookieUtils cookieUtils = new CookieUtils();
        ArrayList<String> list = new ArrayList<>();
        list.add("http://weixin.sogou.com/");
        list.add("http://pb.sogou.com/pv.gif?uigs_t=1483950070110&uigs_productid=weixin&type=weixin_search_pc&pagetype=index&wuid=undefined&uigs_uuid=1483950069921410&login=0&uigs_refer=&");
        list.add("http://weixin.sogou.com/antispider/?from=%2fweixin%3Ftype%3d2%26query%3d%E5%91%A8%E6%98%9F%E9%A9%B0%26ie%3dutf8%26_sug_%3dn%26_sug_type_%3d1%26w%3d01015002%26oq%3d%26ri%3d4%26sourceid%3dsugg%26sut%3d0%26sst0%3d1484046908753%26lkt%3d0%2C0%2C0%26p%3d40040108");
        list.add("http://weixin.sogou.com/antispider/thank.php");
        System.out.println(cookieUtils.getWebCookie(list));
    }

    @Test
    public void test1(){
        String url = "http://weixin.sogou.com/weixin?type=2&ie=utf8&_sug_=n&_sug_type_=&query=深圳";
        CookieUtils cookieUtils = new CookieUtils();
        ArrayList<String> list = new ArrayList<>();
        list.add(url);
        System.out.println(cookieUtils.getWebCookie(list));
    }
}
