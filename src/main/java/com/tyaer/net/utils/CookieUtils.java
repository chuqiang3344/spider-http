package com.tyaer.net.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Twin on 2017/1/10.
 */
public class CookieUtils {
    // 创建CookieStore实例
//    private CookieStore cookieStore = null;
//    private HttpClientContext context = null;
    private static RequestConfig defaultRequestConfig;

    static {
        //RequestConfig基本设置
        defaultRequestConfig = RequestConfig
                .custom()
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)//获取cookie时必须设置，光set可以设置为IGNORE_COOKIES
//                .setExpectContinueEnabled(true)//期望连接
//                .setStaleConnectionCheckEnabled(true) //检查旧连接
//                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
//                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
    }

    // 创建一个本地Cookie存储的实例
    CookieStore cookieStore = new BasicCookieStore();
    //创建一个本地上下文信息
    HttpContext localContext = new BasicHttpContext();

    public String getCookieStr(List<Cookie> cookieList) {
        StringBuffer buffer = new StringBuffer();
        for (Cookie cookie : cookieList) {
            buffer.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        String cookie_str = buffer.toString();
        return cookie_str;
    }

    public String getWebCookie(List<String> urls) {
        String cookieStr = null;
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        for (String url : urls) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.2.2; zh-cn; GT-P5210 Build/JDQ39E) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 MicroMessenger/6.3.30.920 NetType/WIFI Language/zh_CN");
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            //在本地上下问中绑定一个本地存储
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpGet, localContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpEntity entity = response.getEntity();
            List<Cookie> cookies = cookieStore.getCookies();
            cookieStr = getCookieStr(cookies);
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpGet.releaseConnection();
        }
        return cookieStr;
    }

}
