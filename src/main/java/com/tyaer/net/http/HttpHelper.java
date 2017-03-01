package com.tyaer.net.http;

//import com.google.common.base.Joiner;
import com.tyaer.net.bean.HttpMethodType;
import com.tyaer.net.bean.RequestBean;
import com.tyaer.net.bean.ResponseBean;
import com.tyaer.net.manager.HttpClientManager;
import com.tyaer.net.manager.HttpClientManagerParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Http工具类
 *
 * @author Twin
 */
public class HttpHelper {
    public static final String REQUEST_TYPE_GET = "get";
    public static final String REQUEST_TYPE_POST = "post";
    private static Logger logger = Logger.getLogger(HttpHelper.class);
    private static HttpHandle httpHandle = new HttpHandle();

    public static void main(String[] args) throws IOException {
        HttpHelper httpHelper = new HttpHelper();
        String url = "http://newpaper.dahe.cn/hnrbncb/html/2016-12/08/content_101627.htm";
        ResponseBean responseBean = httpHelper.sendRequest(url);
        System.out.println(responseBean.getCharset());
        System.out.println(responseBean.getRawText());
    }

    public static ResponseBean sendRequest(String url) {
        // 搜索链接
        HttpGet httpGet;
        try {
            httpGet = new HttpGet(url);
        } catch (IllegalArgumentException e) {
            logger.error(ExceptionUtils.getMessage(e));
            return null;
        }
        RequestConfig.Builder requestConfigBuilder = HttpClientManager.getRequestConfig();
        httpGet.setConfig(requestConfigBuilder.build());
        return sendRequstGetPage(httpGet);
    }

    /**
     * 超级请求发送
     *
     * @param request
     * @return
     */
    public static ResponseBean sendRequstGetPage(HttpRequestBase request) {
        ResponseBean page = new ResponseBean();
        String url = request.getURI().toString();
        page.setUrl(url);
        RequestConfig config = request.getConfig();
        if (config != null) {
            page.getRequest().setHttpHost(config.getProxy());
        }
        CloseableHttpClient httpClient;
        if (url.startsWith("https")) {
            httpClient = HttpClientManager.createSSLClientDefault();
        } else {
            httpClient = HttpClientManager.getHttpClient();
        }
        /**设置请求头*/
        setHttpRequestHeader(request, page);
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        ByteArrayBuffer byteArrayBuffer = null;
        int statusCode = 0;
        String html = null;
        try {
            long startTime = System.currentTimeMillis();
            response = callableAndFuture(httpClient, request);
            long ping = System.currentTimeMillis() - startTime;
            page.setPing(ping);
            statusCode = httpHandle.getStatusCode(response);
            if (statusCode == 200) {
                statusCode = 666;
                entity = response.getEntity();
                byteArrayBuffer = httpHandle.readToByteBuffer(entity);
                String charset = httpHandle.getCharset(entity, byteArrayBuffer);
                html = new String(byteArrayBuffer.toByteArray(), charset);
                page.setCharset(charset);
                statusCode = 200;
            } else {
                logger.error(statusCode + ": " + url);
                html = statusCode + "";
            }
        } catch (Exception e) {
            logger.error("request fail url: " + url);
            html = ExceptionUtils.getStackTrace(e);
            logger.error(statusCode + ": " + html);
        } finally {
            //关闭
            if (byteArrayBuffer != null) {
                byteArrayBuffer.clear();
            }
            try {
                EntityUtils.consume(entity);// TODO: 2016/9/17 异常，导致无法回收连接？
            } catch (IOException e) {
                logger.error(ExceptionUtils.getMessage(e));
                EntityUtils.consumeQuietly(entity);//继续尝试关闭
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error(ExceptionUtils.getMessage(e));
                }
            }
            //httpclient必须releaseconnection，但不是abort。因为releaseconnection是归还连接到连接池，而abort是直接抛弃这个连接，而且占用连接池的数目。
            request.releaseConnection();
            request.abort();// TODO 关键？连接池资源回收
        }
        page.setStatusCode(statusCode);
        page.setRawText(html);
        return page;
    }

    private static CloseableHttpResponse callableAndFuture(CloseableHttpClient httpClient, HttpRequestBase request) throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExecuteRequsetTask executeRequsetTask = new ExecuteRequsetTask(httpClient, request);
        Future<CloseableHttpResponse> future = executorService.submit(executeRequsetTask);
        CloseableHttpResponse closeableHttpResponse = null;
        try {
            closeableHttpResponse = future.get(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw e;
//            e.printStackTrace();
        } catch (ExecutionException e) {
            throw e;
        } catch (TimeoutException e) {
            logger.error("线程阻塞！ " + request.getURI().toString() + "，" + request.getConfig().getProxy());
            throw e;
        } finally {
            future.cancel(true);
            executorService.shutdownNow();
//            System.out.println(future.cancel(true));
//            System.out.println(future.isCancelled());
//            System.out.println(future.isDone());
        }
        return closeableHttpResponse;
    }

    public static void setHttpRequestHeader(HttpRequestBase httpRequestBase, ResponseBean page) {
        // 搜索头
        String userAgent = HttpClientManagerParams.getRandomUsersAgent();
//        String userAgent = "Mozilla/5.0";
//        String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; Alexa Toolbar; TencentTraveler 4.0)";
        page.getRequest().setUserAgent(userAgent);

        httpRequestBase.addHeader("User-Agent", userAgent);
        httpRequestBase.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        // httpGet.addHeader("Accept-Encoding", "gzip,deflate,sdch");//乱码
        httpRequestBase.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpRequestBase.setHeader("Cache-Control", "no-cache");
        httpRequestBase.setHeader("Pragma", "no-cache");
//        httpRequestBase.setHeader("Connection", "Keep-Alive");
        httpRequestBase.setHeader("Connection", "close"); //会有传输中断异常TruncatedChunkException

        //新浪微博
//        httpRequestBase.setHeader("Host","s.weibo.com");

        //360搜索
//        httpGet.setHeader("DNT", "1");
//        httpGet.setHeader("Cookie", "QiHooGUID=BCCED06C1396157A367C702EE380AC78.1472091798825; _S=boqgjb0b7lo9p0q6j77hi1dej4; tso_Anoyid=11147209179818301550; __guid=15484592.4186740756063152600.1472091795615.492; dpr=1; webp=1; __huid=10t3%2FKzOJYcnPr8T9%2BSJEKcBnxkqrhZb3Kn0Awd27WscA%3D; gtHuid=1; stc_ls_sohome=(TUR_R~4TRW00GXW; oftenso=0; __sid=15484592.4186740756063152600.1472091795615.492.1472091845267; count=5");
//        httpGet.setHeader("Host", "www.so.com");
//        httpGet.setHeader("Referer", "https://www.so.com/s?q=%E9%87%91%E7%89%8C&src=srp&fr=tab_news&psid=ee2880f206f2d0a5cf3d418bf4a5267e");
//        httpGet.setHeader("X-Requested-With", "XMLHttpRequest");

        //百度
//        httpGet.setHeader("Host", "tieba.baidu.com");
//        httpGet.setHeader("Referer", "https://www.so.com/s?q=%E9%87%91%E7%89%8C&src=srp&fr=tab_news&psid=ee2880f206f2d0a5cf3d418bf4a5267e");
//        httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
    }

    public static ResponseBean sendRequest(String url, String cookie) {
        // 搜索链接
        HttpGet httpGet;
        try {
            httpGet = new HttpGet(url);
        } catch (IllegalArgumentException e) {
            logger.error(ExceptionUtils.getMessage(e));
            return null;
        }
        RequestConfig.Builder requestConfigBuilder = HttpClientManager.getRequestConfig();
        httpGet.setConfig(requestConfigBuilder.build());
        if (StringUtils.isNotBlank(cookie)) {
            httpGet.setHeader("Cookie", cookie);
        }
        return sendRequstGetPage(httpGet);
    }

    public static ResponseBean sendRequest(RequestBean requestBean) {
        String url = requestBean.getUrl();
        HttpRequestBase httpRequestBase = null;
        String type = requestBean.getType();
        switch (type) {
            case "get":
                // 搜索链接
                httpRequestBase = new HttpGet(url);
                break;
            case "post":
                httpRequestBase = new HttpPost(requestBean.getUrl());
                HashMap<String, String> parameter = requestBean.getParameter();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (String key : parameter.keySet()) {
                    params.add(new BasicNameValuePair(key, parameter.get(key)));
                }
                try {
                    UrlEncodedFormEntity eparams = new UrlEncodedFormEntity(params, "utf-8");
                    ((HttpPost) httpRequestBase).setEntity(eparams);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("类型错误！");
                return null;
        }
        RequestConfig.Builder requestConfigBuilder = HttpClientManager.getRequestConfig();
        requestConfigBuilder.setProxy(requestBean.getHttpHost());
        RequestConfig requestConfig = requestConfigBuilder.build();
        httpRequestBase.setConfig(requestConfig);
        requestBean.getHeader().setHttpHeader(httpRequestBase);

        ResponseBean page = new ResponseBean();
        page.setUrl(url);
        page.setRequest(requestBean);
        CloseableHttpClient httpClient;
        if (url.startsWith("https")) {
            httpClient = HttpClientManager.createSSLClientDefault();
        } else {
            httpClient = HttpClientManager.getHttpClient();
        }
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        ByteArrayBuffer byteArrayBuffer = null;
        int statusCode = 0;
        String html = null;
        try {
            long startTime = System.currentTimeMillis();
            response = callableAndFuture(httpClient, httpRequestBase);
            long ping = System.currentTimeMillis() - startTime;
            page.setPing(ping);
            statusCode = httpHandle.getStatusCode(response);
            if (statusCode == 200) {
                statusCode = 666;
                entity = response.getEntity();
                byteArrayBuffer = httpHandle.readToByteBuffer(entity);
                String charset = httpHandle.getCharset(entity, byteArrayBuffer);
                html = new String(byteArrayBuffer.toByteArray(), charset);
                page.setCharset(charset);
                statusCode = 200;
            } else {
                logger.error(statusCode + ": " + url);
                html = statusCode + "";
            }
        } catch (Exception e) {
//            logger.error("logger，requestUrl: " + url + "，" + page.getHttpHost());
            html = ExceptionUtils.getMessage(e);
            logger.error(statusCode + ": " + html);
//            logger.error(statusCode + ":" + ExceptionUtils.getStackTrace(e) + "\n" + html);
//            System.out.println(html = ExceptionUtils.getStackTrace(e));
        } finally {
            //关闭
            if (byteArrayBuffer != null) {
                byteArrayBuffer.clear();
            }
            try {
                EntityUtils.consume(entity);// TODO: 2016/9/17 异常，导致无法回收连接？
            } catch (IOException e) {
                logger.error(ExceptionUtils.getMessage(e));
                EntityUtils.consumeQuietly(entity);//继续尝试关闭
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error(ExceptionUtils.getMessage(e));
                }
            }
            //httpclient必须releaseconnection，但不是abort。因为releaseconnection是归还连接到连接池，而abort是直接抛弃这个连接，而且占用连接池的数目。
            httpRequestBase.releaseConnection();
        }
        page.setStatusCode(statusCode);
        page.setRawText(html);
        return page;
    }

    /**
     * 使用代理的get请求
     */
    public static ResponseBean sendRequest(String url, HttpHost httpHost) {
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        RequestConfig.Builder requestConfigBuilder = HttpClientManager.getRequestConfig();
        if (httpHost != null) {
            requestConfigBuilder.setProxy(httpHost);
        }
        RequestConfig requestConfig = requestConfigBuilder.build();
        httpGet.setConfig(requestConfig);
        return sendRequstGetPage(httpGet);
    }

    /**
     * 使用代理的get请求
     *
     * @throws Exception
     */
    public static ResponseBean sendRequest(String url, HttpHost httpHost, String cookie) {
        // 搜索链接
        HttpGet httpGet;
        try {
            httpGet = new HttpGet(url);
        } catch (IllegalArgumentException e) {
            logger.error(ExceptionUtils.getMessage(e));
            return null;
        }
        RequestConfig.Builder requestConfigBuilder = HttpClientManager.getRequestConfig();
        if (httpHost != null) {
            requestConfigBuilder.setProxy(httpHost);
        }
        RequestConfig requestConfig = requestConfigBuilder.build();
        httpGet.setConfig(requestConfig);
        //设置cookies
        if (StringUtils.isNotBlank(cookie)) {
            httpGet.setHeader("Cookie", cookie);
        }
        return sendRequstGetPage(httpGet);
    }

    public static ResponseBean sendRequstGetPage(HttpRequestBase request, HttpContext httpContext) {
        ResponseBean page = new ResponseBean();
        String html;
        String url = request.getURI().toString();
        CloseableHttpClient httpClient;
        if (url.startsWith("https")) {
            httpClient = HttpClientManager.createSSLClientDefault();
        } else {
            httpClient = HttpClientManager.getHttpClient();
//            httpClient = HttpClients.createDefault();
        }
        page.setUrl(url);
        /**设置请求头*/
        setHttpRequestHeader(request, page);
        try {
            CloseableHttpResponse response = httpClient.execute(request, httpContext);
            HttpEntity entity = response.getEntity();
            ByteArrayBuffer byteArrayBuffer = httpHandle.readToByteBuffer(entity);
            String charset = httpHandle.getCharset(entity, byteArrayBuffer);
            page.setCharset(charset);
            html = new String(byteArrayBuffer.toByteArray(), charset);
            int statusCode = httpHandle.getStatusCode(response);
            page.setStatusCode(statusCode);
            //关闭
            EntityUtils.consume(entity);
            response.close();
        } catch (IOException e) {
            logger.error("logger，requestUrl: " + url);
            logger.error(ExceptionUtils.getMessage(e));
            html = ExceptionUtils.getMessage(e);
        } finally {
            request.releaseConnection();
        }
        page.setRawText(html);
        return page;
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
//        String authHeader = "MYH-AUTH-MD5 " + Joiner.on('&').withKeyValueSeparator("=").join(paramMap);
        String authHeader = "";
        return authHeader;
    }

    /**
     * 模拟发出Http请求
     *
     * @param url    请求资源,如：http://www.baidu.com/,注意严谨的格式
     * @param params 请求参数
     * @param type   请求方式,目前只支持get/post
     * @return HttpResponseBody
     * @throws IOException
     * @throws ClientProtocolException
     */
    public ResponseBean sendRequest(String url, String type, Map<String, String> params) {
        ResponseBean page = null;
        String html = "";
        // GET方式请求
//        if(type.equals(HttpMethodType.PUT))
        if (HttpHelper.REQUEST_TYPE_GET.equals(type)) {
            // 加入请求参数
            if (params != null) {
                if (url.indexOf("?") != -1) {
                    url += "&";
                } else {
                    url += "?";
                }
                for (String key : params.keySet()) {
                    url += key + "=" + params.get(key) + "&";
                }
            }
            HttpGet httpGet = new HttpGet(url);
            // 模拟浏览器
            page = sendRequstGetPage(httpGet);
        } else if (HttpHelper.REQUEST_TYPE_POST.equals(type)) {
            // POST方式请求
            HttpPost httpPost = new HttpPost(url);
            // 加入请求参数
            if (params != null) {
                List<BasicNameValuePair> paramList = new ArrayList<BasicNameValuePair>();
                for (String key : params.keySet()) {
                    if (key != null) {
                        paramList.add(new BasicNameValuePair(key, params
                                .get(key)));
                    }
                }
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            page = sendRequstGetPage(httpPost);
        }
        return page;
    }

    public ResponseBean sendPostRequest(String url, Map<String, String> prams) {
        HttpPost httppost = new HttpPost(url);
        // All the parameters post to the web site
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
        for (String key : prams.keySet()) {
            nvps.add(new BasicNameValuePair(key, prams.get(key)));
        }
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sendRequstGetPage(httppost);
    }

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

    public void setRequestBuilderHeader(RequestBuilder requestBuilder) {
        // 搜索头
        String userAgent = HttpClientManagerParams.getRandomUsersAgent();
//        String userAgent = "Mozilla/5.0";
//        String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; Alexa Toolbar; TencentTraveler 4.0)";
        requestBuilder.addHeader("User-Agent", userAgent);
        requestBuilder.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        // httpGet.addHeader("Accept-Encoding", "gzip,deflate,sdch");//乱码
        requestBuilder.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        requestBuilder.setHeader("Cache-Control", "no-cache");
        requestBuilder.setHeader("Pragma", "no-cache");
//        httpRequestBase.setHeader("Connection", "Keep-Alive");
        requestBuilder.setHeader("Connection", "close"); //会有传输中断异常TruncatedChunkException
    }

    static class ExecuteRequsetTask implements Callable<CloseableHttpResponse> {
        CloseableHttpClient httpClient;
        HttpRequestBase request;

        public ExecuteRequsetTask(CloseableHttpClient httpClient, HttpRequestBase request) {
            this.httpClient = httpClient;
            this.request = request;
        }

        @Override
        public CloseableHttpResponse call() throws Exception {
//            System.out.println("子线程在进行计算");
            CloseableHttpResponse response = httpClient.execute(request, HttpClientContext.create());
            return response;
        }
    }
}
