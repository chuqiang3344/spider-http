package com.tyaer.net.http;

import com.google.common.base.Joiner;
import com.tyaer.net.bean.ResponseBean;
import com.tyaer.net.manager.HttpClientManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tyaer.net.http.HttpHelper.sendRequstGetPage;

/**
 * 私密代理
 * Created by Twin on 2017/3/1.
 */
public class ProxyServer {

    private static final Logger logger = Logger.getLogger(ProxyServer.class);

    /**
     * 大蚂蚁
     * 使用代理的get请求
     *
     * @throws Exception
     */
    public ResponseBean sendRequestDMY(String url, String cookie) {
        // 搜索链接
        HttpGet httpGet;
        try {
            httpGet = new HttpGet(url);
        } catch (IllegalArgumentException e) {
            logger.error(ExceptionUtils.getMessage(e));
            return null;
        }
        RequestConfig.Builder requestConfigBuilder = HttpClientManager.getRequestConfig();
        HttpHost proxy = new HttpHost("123.57.11.143", 8123, "http");
        requestConfigBuilder.setProxy(proxy);
        RequestConfig requestConfig = requestConfigBuilder.build();
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("Proxy-Authorization", getAuthHeader());// authheader的生成方法参照网站的程序
        //设置cookies
        if (StringUtils.isNotBlank(cookie)) {
            httpGet.setHeader("Cookie", cookie);
        }
        return sendRequstGetPage(httpGet);
    }

    private String getAuthHeader() {
        // 定义申请获得的appKey和appSecret
        String appkey = "82789299";
        String secret = "3d0279cdbe3ef60f90f40d07c985535d";

        // 创建参数表
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("app_key", appkey);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));//使用中国时间，以免时区不同导致认证错误
        paramMap.put("timestamp", format.format(new Date()));

        // 对参数名进行排序
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);

        // 拼接有序的参数名-值串
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(secret);
        for (String key : keyArray) {
            stringBuilder.append(key).append(paramMap.get(key));
        }

        stringBuilder.append(secret);
        String codes = stringBuilder.toString();

        // MD5编码并转为大写， 这里使用的是Apache codec
        String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(codes).toUpperCase();

        paramMap.put("sign", sign);

        // 拼装请求头Proxy-Authorization的值，这里使用 guava 进行map的拼接
        String authHeader = "MYH-AUTH-MD5 " + Joiner.on('&').withKeyValueSeparator("=").join(paramMap);
        return authHeader;
    }

    public ResponseBean sendRequestAccount(String url, HttpHost proxy) {
        // 搜索链接
        HttpGet httpGet;
        try {
            httpGet = new HttpGet(url);
        } catch (IllegalArgumentException e) {
            logger.error(ExceptionUtils.getMessage(e));
            return null;
        }
        BasicScheme proxyAuth = new BasicScheme();
        // Make client believe the challenge came form a proxy
        try {
            proxyAuth.processChallenge(new BasicHeader(AUTH.PROXY_AUTH, "BASIC realm=default"));
        } catch (MalformedChallengeException e) {
            e.printStackTrace();
        }
        BasicAuthCache authCache = new BasicAuthCache();
        authCache.put(proxy, proxyAuth);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(proxy),
                new UsernamePasswordCredentials("liuyq", "r51ex0kc"));
        HttpClientContext context = HttpClientContext.create();
        context.setAuthCache(authCache);
        context.setCredentialsProvider(credsProvider);
        return sendRequstGetPage(httpGet, context);
    }

    private void setProxy(HttpClientContext context) {
        HttpHost targetHost = new HttpHost("localhost", 80, "http");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials("username", "password"));
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);
        // Add AuthCache to the execution context
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);
    }
}
