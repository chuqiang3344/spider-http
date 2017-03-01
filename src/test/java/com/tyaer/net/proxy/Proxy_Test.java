package com.tyaer.net.proxy;

import com.tyaer.net.bean.ResponseBean;
import com.tyaer.net.http.HttpHelper;
import org.apache.http.HttpHost;
import org.junit.Test;

/**
 * Created by Twin on 2017/2/22.
 */
public class Proxy_Test {
    public static void main(String[] args) {
        HttpHost httpHost = new HttpHost("39.86.60.118", 9999);
        ResponseBean responseBean = HttpHelper.sendRequest("http://www.secretchina.com/",httpHost);
        System.out.println(responseBean);
    }

    @Test
    public void foreign_proxy(){
//        HttpHost httpHost=null;
        HttpHost httpHost = new HttpHost("svnserver", 808);
        String url = "https://www.google.com.hk/search?q=%E7%BD%97%E6%9B%BC%E8%92%82%E5%85%8B&num=50&safe=strict&tbm=nws&ei=YRBRWJeWE8Kg0gSg2IDADw&start=0&sa=N&biw=1441&bih=423&dpr=1";
        ResponseBean responseBean = HttpHelper.sendRequest(url,httpHost);
        System.out.println(responseBean);
    }
}
