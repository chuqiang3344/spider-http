package com.tyaer.net.utils;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Twin on 2016/12/19.
 */
public class UrlUtils {
    public static void main(String[] args) {
//        String url="http://weixin.sogou.com/weixin?query=%E6%91%A9%E6%8B%9C&_sug_type_=&_sug_=n&type=2&page=2&ie=utf8";
//        String url="http://weixin.sogou.com/weixinwap?page=1&_rtype=json&ie=utf8&type=2&t=1483061392733&query=%E6%91%A9%E6%8B%9C&pg=webSearchList&_sug_=y&_sug_type_=&tsn=1&";
//        analyzeParameters(url);

        String url = "http://gocn.southcn.com/tpzx2010/qxpic/";
        String transformUrl = get302Url(url);
        System.out.println(transformUrl);

    }

    public static String get302Url(String loginUrl) {

//        PoolingHttpClientConnectionManager connectionManager=new PoolingHttpClientConnectionManager();
//        connectionManager.setMaxTotal(100);
//        connectionManager.setDefaultMaxPerRoute(20);

        CloseableHttpClient client = HttpClients.custom()
//                .setConnectionManager(connectionManager)
                .setRedirectStrategy(new LaxRedirectStrategy())// 声明重定向策略对象
                .disableRedirectHandling() //关闭重定向
                .build();

        String location = null;
        HttpGet httpGet = new HttpGet(loginUrl);
        httpGet.setHeader("Connection", "close"); //会有传输中断异常TruncatedChunkException
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行post请求
            httpResponse = client.execute(httpGet, new HttpClientContext());
//            printResponse(httpResponse);
            Header locationHeader = httpResponse.getFirstHeader("Location");
            if (locationHeader != null) {
                location = locationHeader.getValue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流并释放资源
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            httpGet.releaseConnection();
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return location;
    }

    /**
     * 不好用，有问题
     *
     * @param urlString
     * @return
     */
    public static URI transformURI(String urlString) {
        if (null == urlString || urlString.isEmpty()) {
            return null;
        }
        //防止传入的urlString首尾有空格
        urlString = urlString.trim();
        //转化String url为URI,解决url中包含特殊字符的情况
        URI uri = null;
        try {
            URL url = new URL(urlString);
            //这里如果会强制将urlString转换为UTF-8格式，如百度贴吧的链接key为gb2312则不能使用此方法转换。
            uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
//            url=new URI()
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * 分析url参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> analyzeParameters(String url) {
        System.out.println(url);
        HashMap<String, String> hashMap = new HashMap<>();
        String[] split = url.split("\\?");
        if (split.length > 1) {
            String parameters_str = split[1];
            String[] parameters = parameters_str.split("&");
            for (String parameter : parameters) {
                String[] split1 = parameter.split("=");
                if (split1.length > 1) {
                    hashMap.put(split1[0], split1[1]);
                }
            }
        }
        System.out.println(hashMap);
        return hashMap;
    }

    /**
     * 分析cookies参数
     *
     * @param cookies
     * @return
     */
    public static Map<String, String> analyzeCookies(String cookies) {
        HashMap<String, String> hashMap = new HashMap<>();
        System.out.println(cookies);
        String[] split = cookies.split(";");
        System.out.println();
        for (String cookie : split) {
            System.out.println(cookie);
            String[] kv = cookie.split("=");
            if (kv.length > 1) {
                hashMap.put(kv[0], kv[1]);
            }
        }
        return hashMap;
    }

    @Test
    public void analyzeCookies_Test() {
        String cookies = "ABTEST=5|1482203525|v1; IPLOC=CN4403; SUID=A1C110B7721A910A000000005858A185; SUID=7AFE0FB71E20910A000000005858A186; SUV=00576323B70FFEBF5858A186C04CB255; weixinIndexVisited=1; PHPSESSID=3q2kl6qcpas0qevj8m40mj2e76; SUIR=1482997507; SNUID=3807F64EF8FCBD3EBD864CFFF9A377FE; JSESSIONID=aaasujALLhsu4gdGrs7Kv; sct=2; seccodeErrorCount=1|Thu, 29 Dec 2016 11:11:30 GMT; seccodeRight=success; successCount=2|Thu, 29 Dec 2016 11:08:01 GMT; refresh=1";
        Map<String, String> stringStringMap = analyzeCookies(cookies);
        System.out.println(stringStringMap);
    }

}
