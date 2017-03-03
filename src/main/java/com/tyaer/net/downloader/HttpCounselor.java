package com.tyaer.net.downloader;

import com.tyaer.net.bean.ResponseBean;
import com.tyaer.net.http.HttpHelper;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Queue;


/**
 * 请求策略
 * Created by Twin on 2017/2/20.
 */
public class HttpCounselor {
    private static final Logger logger = Logger.getLogger(HttpCounselor.class);
    private static final long pause_time = 4 * 60 * 1000;
    private static volatile boolean useProxy = false;
    private static volatile long useProxy_time = 0;


    /**
     * 快速模式 or 代理模式
     * @param url
     * @param cookie
     * @param queue
     * @return
     */
    public static ResponseBean getArticleHtml(String url, String cookie, Queue<HttpHost> queue) {
        ResponseBean responseBean;
        if (useProxy) {
            if (Calendar.getInstance().getTimeInMillis() - useProxy_time > pause_time) {
                logger.warn("###重新启用快速访问模式...");
                useProxy = false;
                useProxy_time = 0;
                return getArticleHtml(url, cookie, queue);
            } else {
                HttpHost httpHost = queue.poll();
                responseBean = HttpHelper.sendRequest(url, httpHost, cookie);
            }
        } else {
            responseBean = HttpHelper.sendRequest(url, cookie);
            if (responseBean.getStatusCode() == 501) {
                logger.warn("###启用切换代理访问模式...");
                useProxy = true;
                useProxy_time = Calendar.getInstance().getTimeInMillis();
                return getArticleHtml(url, cookie, queue);
            }
        }
        return responseBean;
    }

    /**
     * 使用代理IP和本机IP请求
     *
     * @param url
     * @param cookie
     * @param retryNum 尝试次数，最后一次不使用代理。
     * @param queue    代理queue
     * @return
     */
    public static ResponseBean mixProxyRequest(String url, String cookie, int retryNum, Queue<HttpHost> queue) {
        ResponseBean responseBean = null;
        while (retryNum > 0) {
            if (retryNum > 1) {
                HttpHost httpHost = queue.poll();
                if (httpHost == null) {
                    logger.warn("queue无可用代理！");
                }
                responseBean = HttpHelper.sendRequest(url, httpHost, cookie);
            } else {
                responseBean = HttpHelper.sendRequest(url, cookie);
            }
            if (responseBean.getStatusCode() == 200) {
                break;
            } else {
                retryNum--;
            }
        }
        return responseBean;
    }
}
