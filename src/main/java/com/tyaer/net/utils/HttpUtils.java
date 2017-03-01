package com.tyaer.net.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Twin on 2016/11/16.
 */
public class HttpUtils {
    private static CloseableHttpClient client =createSSLClientDefault();

    private static CookieStore cookieStore;
    private static HttpContext localContext;

    static {
        // 创建一个本地Cookie存储的实例
        cookieStore = new BasicCookieStore();
        //创建一个本地上下文信息
        localContext = new BasicHttpContext();
        //在本地上下问中绑定一个本地存储
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    /**
     * 描述：针对https采用SSL的方式创建httpclient
     *
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public String downLoadImg(String url){
        HttpGet httpGet = new HttpGet(url);
        String result = null;
        try {
            CloseableHttpResponse httpResponse = client.execute(httpGet);
            InputStream inputStream = httpResponse.getEntity().getContent();
            File file = new File("./file/baidu.jpg");
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();

            outputStream.close();
            inputStream.close();
            result=file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            httpGet.releaseConnection();
        }
        return result;
    }

    public static String getCookieStr(List<Cookie> cookieList) {
        StringBuffer buffer = new StringBuffer();
        for (Cookie cookie : cookieList) {
            buffer.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        String cookie_str = buffer.toString();
        return cookie_str;
    }


    public static String post(String loginUrl, HashMap<String,String> paramsMap){
        HttpPost post = new HttpPost(loginUrl);
        post.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build());
        post.setHeader("User-Agent", "Mozilla/5.0(Windows;U;WindowsNT6.1;rv:2.2)Gecko/20110201");
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        post.setHeader("Referer", "http://weixin.sogou.com/antispider/?from=%2fweixin%3Ftype%3d2%26query%3d%E8%87%AA%E8%A1%8C%E8%BD%A6%26ie%3dutf8%26_sug_%3dy%26_sug_type_%3d");
        post.setHeader("Host", "weixin.sogou.com");
//        post.setHeader("Origin", "http://tieba.baidu.com");
//        String cookie="BAIDUID=36BED8CAD0D134B4A713ED4D0F0B8119:FG=1;BDUSS=BvYlFBSmc1a1JqUUFXamR3NGxVMFNubWh2Y3VNb3NFYnpoVzRDQjRXWDRHVkZZSVFBQUFBJCQAAAAAAAAAAAEAAACd8KMIY2h1cWlhbmczMzQ0AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPiMKVj4jClYNV;BIDUPSID=36BED8CAD0D134B4A713ED4D0F0B8119;PSTM=1479118073;BDRCVFR[laz3IxV61qm]=mbxnW11j9Dfmh7GuZR8mvqV;BD_HOME=1;H_PS_PSSID=17942;__bsi=13197132025009865138_00_14_N_N_97_0303_C02F_N_N_N_0;BD_UPN=1123314251;";
//        String cookie="BAIDUID=08B969F39AABC5AF0CDFF942237DB6E1:FG=1; BIDUPSID=08B969F39AABC5AF0CDFF942237DB6E1; PSTM=1479108894; BDRCVFR[EzYDMnK0Xs6]=mk3SLVN4HKm; BDRCVFR[laz3IxV61qm]=mk3SLVN4HKm; TIEBAUID=512142465a71521c730a710a; TIEBA_USERTYPE=9d2ff1ebe93dd527e01c2f37; bdshare_firstime=1479118177616; Hm_lvt_287705c8d9e2073d13275b18dbd746dc=1479118179; Hm_lpvt_287705c8d9e2073d13275b18dbd746dc=1479118179; H_PS_PSSID=1450_21422_17948_21080_18559_21455_21409_21378_21526_21190_21306; BDUSS=R2UU1YVzVKTW1zaW52eFBlenhVbVRlLVlLVVZJWGpEakRmN3lCeVpQSUNJMUZZSVFBQUFBJCQAAAAAAAAAAAEAAACWZkiOOTZhMzc2NAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKWKVgClilYMF; wise_device=0; LONGID=2387109526";
//        String cookie="ABTEST=0|1484097170|v1; IPLOC=CN4403; SUID=FBFD0FB7771A910A0000000058758692; SUID=FBFD0FB73320910A0000000058758693; weixinIndexVisited=1; SUV=0048178AB70FFDFB58758693A293E694; JSESSIONID=aaaPQEfJP1gZHh4NNx7Kv; PHPSESSID=vcc6af770cq97jsevfkoa2btu2; SUIR=1484097221";
//        String cookie="ABTEST=0|1484097170|v1; IPLOC=CN4403; SUID=FBFD0FB7771A910A0000000058758692; SUID=FBFD0FB73320910A0000000058758693; weixinIndexVisited=1; SUV=0048178AB70FFDFB58758693A293E694; JSESSIONID=aaaPQEfJP1gZHh4NNx7Kv; PHPSESSID=vcc6af770cq97jsevfkoa2btu2; SUIR=1484097221";
        String cookie="ABTEST=0|1484095795|v1;IPLOC=CN4403;PHPSESSID=vcc6af770cq97jsevfkoa2btu2;SUID=FBFD0FB7771A910A0000000058758133;SUIR=1484095796;SUV=00525A9FB70FFDFB58758134637CD963;";
        post.setHeader("Cookie",cookie);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : paramsMap.keySet()) {
            params.add(new BasicNameValuePair(key, paramsMap.get(key)));
        }
        String result = "";
        try {
            UrlEncodedFormEntity eparams = new UrlEncodedFormEntity(params, "utf-8");
            post.setEntity(eparams);
            HttpResponse response = client.execute(post, localContext);
            HttpEntity entity = response.getEntity();
            List<Cookie> cookies = cookieStore.getCookies();
            String cookieStr = getCookieStr(cookies);
            System.out.println(cookieStr);
            result=IOUtils.toString(entity.getContent(),"utf-8");

            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
//        System.out.println("cookie:" + result);
        return result;
    }

    public static String get(String url,String cookie){
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build());
//        httpGet.setHeader("Cookie", cookie);
//        httpGet.setHeader("User-Agent", "Mozilla/5.0(Windows;U;WindowsNT6.1;rv:2.2)Gecko/20110201");
//        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.0.1; MX4 Build/LRX22C) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrowser/6.8 TBS/036872 Safari/537.36 MicroMessenger/6.3.30.920 NetType/WIFI Language/zh_CN");
//        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.2.2; zh-cn; GT-P5210 Build/JDQ39E) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 MicroMessenger/6.3.30.920 NetType/WIFI Language/zh_CN");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        httpGet.setHeader("Host", "mp.weixin.qq.com");
//        httpGet.setHeader("Referer", "https://www.baidu.com/");
//        httpGet.setHeader("Origin", "https://login.sina.com.cn");

        String result=null;
        try {
            // 创建一个本地Cookie存储的实例
            CookieStore cookieStore = new BasicCookieStore();
            //创建一个本地上下文信息
            HttpContext localContext = new BasicHttpContext();
            //在本地上下问中绑定一个本地存储
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            HttpResponse response = client.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            List<Cookie> cookies = cookieStore.getCookies();
//            result = HttpHelper.getCookieStr(cookies);
            result=IOUtils.toString(entity.getContent(),"utf-8");

            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpGet.releaseConnection();
        }
//        System.out.println("cookie:" + result);
        return result;
    }

    public static String getWebCookie(String url,String cookie){
        String cookieStr = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build());
//        httpGet.setHeader("User-Agent", "Mozilla/5.0(Windows;U;WindowsNT6.1;rv:2.2)Gecko/20110201");
//        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.0.1; MX4 Build/LRX22C) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile MQQBrowser/6.8 TBS/036872 Safari/537.36 MicroMessenger/6.3.30.920 NetType/WIFI Language/zh_CN");
//        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.2.2; zh-cn; GT-P5210 Build/JDQ39E) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 MicroMessenger/6.3.30.920 NetType/WIFI Language/zh_CN");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpGet.setHeader("Cookie", cookie);
//        httpGet.setHeader("Host", "mp.weixin.qq.com");
//        httpGet.setHeader("Referer", "https://www.baidu.com/");
//        httpGet.setHeader("Origin", "https://login.sina.com.cn");

//        String result=null;
        try {
            // 创建一个本地Cookie存储的实例
            CookieStore cookieStore = new BasicCookieStore();
            //创建一个本地上下文信息
            HttpContext localContext = new BasicHttpContext();
            //在本地上下问中绑定一个本地存储
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            List<Cookie> cookies = cookieStore.getCookies();
            cookieStr = getCookieStr(cookies);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpGet.releaseConnection();
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cookieStr;
    }



    public static void main(String[] args) {
        String url="http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=MTI0MDU3NDYwMQ==#wechat_webview_type=1&wechat_redirect";
        String s = get(url,"");
        System.out.println(s);
    }
}
