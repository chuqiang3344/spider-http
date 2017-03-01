package com.tyaer.net.manager;

import com.tyaer.net.bean.DTO;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient连接池管理
 * Created by Twin on 2016/8/16.
 */
public class HttpClientManager {
    private static final Logger logger = Logger.getLogger(HttpClientManager.class);
    /**
     * 下载失败后，马上进行重试的次数(通过使用httpclient自带的requestretryhandler机制)
     */
    private static int retryTimes;
    /**
     * 最大连接数
     */
    private static int MAX_TOTAL_CONNECTIONS;
    /**
     * 每个路由最大连接数,设置每个主机地址的并发数
     * 对每个指定连接的服务器（指定的ip）可以创建并发20 socket进行访问
     */
    private static int MAX_ROUTE_CONNECTIONS;
    /**
     * 下载失败后，将reqest重新加入队列，重新尝试下载的次数(添加到page的target request中，进入下一轮循环，也有可能被发送到MQ中)
     */
//    private static final int cycleRetryTimes = 0;
//    private static final int timeOut = 5000;
    /**
     * 获取连接的最大等待时间
     */
    private static int CONNECTION_TIMEOUT;
    /**
     * 读取超时时间
     */
    private static int SOCKET_TIMEOUT;
    /**
     * 获取httpclient的最大等待时间
     */
    private static int CONNECTION_REQUEST_TIMEOUT;

    // private static HttpParams httpParams;
    private static PoolingHttpClientConnectionManager connectionManager;
    private static ConnectionKeepAliveStrategy myStrategy;
    private static HttpRequestRetryHandler myRequestRetryHandler;
    private static RequestConfig defaultRequestConfig;
    private static CloseableHttpClient CLOSEABLEHTTPCLIENT_INSTANCE;

    static {
        MAX_TOTAL_CONNECTIONS = DTO.MAX_TOTAL_CONNECTIONS;
        MAX_ROUTE_CONNECTIONS = DTO.MAX_ROUTE_CONNECTIONS;
        CONNECTION_TIMEOUT = DTO.CONNECTION_TIMEOUT;
        SOCKET_TIMEOUT = DTO.SOCKET_TIMEOUT;

        CONNECTION_REQUEST_TIMEOUT = 10000;

        if (connectionManager == null) {
//            connectionManager = getHttpsConnectionManager(); //Https
            connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
            connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
//            connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("weibo.com")), 150);//每个路由器对每个服务器允许最大的并发访问
        }

        // 连接回收策略,启动线程，5秒钟清空一次失效连接
//        new IdleConnectionMonitorThread(connectionManager).start();//// TODO: 2016/12/8 线程

        // 连接存活策略
        myStrategy = new MyDefaultConnectionKeepAliveStrategy();
        // 请求重试策略
        retryTimes = 2;
        myRequestRetryHandler = new MyDefaultHttpRequestRetryHandler(retryTimes);

        //RequestConfig基本设置
        defaultRequestConfig = RequestConfig
                .custom()
                .setCookieSpec(CookieSpecs.STANDARD)//获取cookie时必须设置，光set可以设置为IGNORE_COOKIES
                .setExpectContinueEnabled(true)//期望连接
//                .setStaleConnectionCheckEnabled(true) //检查旧连接
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
    }

    private static PoolingHttpClientConnectionManager getHttpsConnectionManager() {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf)
                .register("https", sslsf)
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        return connectionManager;
    }

    /**
     * request设置
     *
     * @return
     */
    public static RequestConfig.Builder getRequestConfig() {
        RequestConfig.Builder builder = RequestConfig
                .copy(defaultRequestConfig)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)//去除关键词任务Warn TODO: 2016/11/26 忽略Cookie的政策。
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
        return builder;
    }

    /**
     * 全局设置
     *
     * @return
     */
    public static RequestConfig getDefaultRequestConfig() {
        RequestConfig requestConfig = RequestConfig
                .copy(defaultRequestConfig)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .build();
        return requestConfig;
    }

    public static CloseableHttpClient getHttpClient() {
        if (CLOSEABLEHTTPCLIENT_INSTANCE == null) {
            CloseableHttpClient httpClient = HttpClients
                    .custom()
                    .setConnectionManager(connectionManager)
//                  .setSSLSocketFactory(getSSLConnectionSocketFactory()) //认证https，若同时设置了connectionManager，该设置无效？
                    .setDefaultRequestConfig(getDefaultRequestConfig())
                    .setDefaultCookieStore(new BasicCookieStore())
                    .setRedirectStrategy(new LaxRedirectStrategy())// 声明重定向策略对象
//                    .disableRedirectHandling() //关闭重定向
                    .setRetryHandler(myRequestRetryHandler) //重试请求.setRetryHandler(new DefaultHttpRequestRetryHandler(retryTimes, true)) //重试请求
                    .setKeepAliveStrategy(myStrategy) //// TODO: 2016/8/16  需要测试
                    .build();//刷新配置
            CLOSEABLEHTTPCLIENT_INSTANCE = httpClient;
        }
        return CLOSEABLEHTTPCLIENT_INSTANCE;
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

    /**
     * SSL/TLS定制
     *
     * @return
     */
    private static SSLConnectionSocketFactory getSSLConnectionSocketFactory() {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有 always return true，trust every certificate type
                public boolean isTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
        return sslsf;
    }

    /**
     * 请求重试机制
     */
    private static class MyDefaultHttpRequestRetryHandler implements HttpRequestRetryHandler {
        private int retryTimes;

        public MyDefaultHttpRequestRetryHandler(int retryTimes) {
            this.retryTimes = retryTimes;
        }

        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount >= retryTimes) {
                // 超过三次则不再重试请求
                return false;
            }
            if (exception instanceof InterruptedIOException) {
                // Timeout
                return false;
            }
            if (exception instanceof UnknownHostException) {
                // Unknown host
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {
                // Connection refused
                return false;
            }
            if (exception instanceof SSLException) {
                // SSL handshake exception
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
//                System.out.println("重试请求...");
                // Retry if the request is considered idempotent 重试，如果该请求被认为是幂等
                return true;
            }
            return false;
        }
    }

    /**
     * 超时类的扩展
     * http长连接策略 可以根据须要定制所须要的长连接策略，可根据服务器指定的超时时间，也可根据主机名自己指定超时时间；
     */
    private static class MyDefaultConnectionKeepAliveStrategy implements
            ConnectionKeepAliveStrategy {

        // 如果服务器有超时则使用
        @Override
        public long getKeepAliveDuration(final HttpResponse response, final HttpContext context) {
//            Args.notNull(response, "HTTP response");
            // 遍历response的header
            HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                final HeaderElement he = it.nextElement();
                final String param = he.getName();
                final String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {//如果头部包含timeout信息，则使用
                    try {
                        //超时时间设置为服务器指定的值
                        long timeout = Long.parseLong(value) * 1000;
//                        System.out.println("timeout:"+timeout);
                        return timeout;
                    } catch (NumberFormatException ignore) {
                    }
                }
            }
            //获取主机
            HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
            if ("webservice.webxml.com.cn".equalsIgnoreCase(target.getHostName())) {
                // 如果访问webservice.webxml.com.cn主机则设置长连接时间为5秒
                return 5 * 1000;
            } else {
                // 其他为30秒
                return 30 * 1000;
            }
        }
    }

    /**
     * 这个线程负责使用连接管理器清空失效连接和过长连接
     */
    private static class IdleConnectionMonitorThread extends Thread {
        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown = false;

        public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            while (!shutdown) {
                try {
                    synchronized (this) {
                        wait(5000);
//						System.out.println("清空失效连接...");
                        // 关闭失效连接
                        connMgr.closeExpiredConnections();
                        // 关闭空闲超过30秒的连接
                        connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                } catch (Exception e) {
                    // terminatee
                    logger.error("连接管理器清理线程异常：" + e);
                }
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}

