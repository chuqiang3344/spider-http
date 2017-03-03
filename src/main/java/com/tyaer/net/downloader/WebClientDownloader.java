package com.tyaer.net.downloader;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.util.Set;

/**
 * cookie获取根据
 *
 * @author Twin
 */
public class WebClientDownloader {

    private static Logger LOGGER = Logger.getLogger(WebClientDownloader.class);

    private static WebClient _WebClient = null;

    static {
        _WebClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        // htmlunit 对css和javascript的支持不好，所以请关闭之。url为JS请求则必须打开！
//		_WebClient.getOptions().setJavaScriptEnabled(false);
        _WebClient.getOptions().setCssEnabled(false);
    }

    public static String getCookie(String str_url) {
        Set<Cookie> set_cookies = null;
        String str_cookie = "";
        Page page = null;
        try {
            // 先得到page,才能拿到cookie
            page = _WebClient.getPage(str_url);
//			URL url_urlReq = new URL(str_url);
//			set_cookies = _WebClient.getCookies(url_urlReq);
            set_cookies = _WebClient.getCookieManager().getCookies();
            if (set_cookies != null) {
                str_cookie = getCookieHeader(set_cookies);
            }
        } catch (Exception e) {
            LOGGER.error("_getCookie请求错误：" + str_url);
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        } finally {
            if (page != null) {
                page.cleanUp();
            }
        }
        System.out.println(str_cookie);
        return str_cookie;
    }

    /**
     * 格式化cookie
     */
    public static String getCookieHeader(Set<Cookie> cookies) {
        StringBuilder cookieBuilder = new StringBuilder();
        for (Cookie cookie : cookies) {
            cookieBuilder.append(cookie.getName() + "=" + cookie.getValue()
                    + ";");
        }
        String cookieStr = "";
        // 去除最后的;
        if (cookieBuilder.length() > 0) {
            cookieStr = cookieBuilder.substring(0, cookieBuilder.length() - 1);
        }
        return cookieStr;
    }

    /**
     * 网站基本信息展示
     *
     * @param str_url
     */
    public static void showBaseInfo(String str_url) {
        WebClient _WebClient = new WebClient();
        // htmlunit 对css和javascript的支持不好，所以请关闭之。JS请求必须打开！
        _WebClient.getOptions().setJavaScriptEnabled(false);
        _WebClient.getOptions().setCssEnabled(false);
        // 获取页面
        HtmlPage page = null;
        try {
            page = _WebClient.getPage(str_url);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getMessage(e));
        } finally {
            if (_WebClient != null) {
                _WebClient.close();
            }
        }
        // 获取页面的TITLE
        String str_title = page.getTitleText();
        System.out.println("标题：" + str_title);
        // 获取页面的XML代码
        String str_html = page.asXml();
        System.out.println("XML:" + str_html);
        // 获取页面的文本
        String str_text = page.asText();
        System.out.println("html:" + str_text);

        String str_textContent = page.getTextContent();
        System.out.println("TextContent:" + str_textContent);
    }

    public static String getHtml(String url) {
//		WebClient webClient = new WebClient();
        // htmlunit 对css和javascript的支持不好，所以请关闭之。JS请求必须打开！
//		_WebClient.getOptions().setJavaScriptEnabled(false);
//		_WebClient.getOptions().setCssEnabled(false);
        // 获取页面
        HtmlPage page = null;
        try {
            page = _WebClient.getPage(url);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getMessage(e));
        } finally {
            if (_WebClient != null) {
                _WebClient.close();
            }
        }
        String str_html = page.asXml();
        return str_html;
    }

    public static void main(String[] args) throws InterruptedException {
        WebClientDownloader util = new WebClientDownloader();
//		String str_url = "http://www.cnblogs.com/Gaojiecai/p/3415146.html";
//		String str_url = "http://mp.weixin.qq.com/profile?src=3&timestamp=1479881197&ver=1&signature=mFCwcLO9hTwe*Js7TGQ457olpvr1d85gJSnVLyFgtYloE8sxxQCrV7BsbGbRDYkDEgHzd1pXjj-qh4UblC-Hug==";
//		String str_url = "http://data.stats.gov.cn/easyquery.htm?cn=E0104";
//        String str_url = "http://m.weibo.cn/u/2640113513";
        String str_url = "http://data.stats.gov.cn/easyquery.htm?cn=E0104";
        String html = util.getHtml(str_url);
        System.out.println(html);

        /*String str_url = "http://www.yxtv.cn/portal.php?mod=list&catid=1";
        String com.tyaer.net.cookie = util.getCookie("http://www.yxtv.cn/");
		System.out.println(com.tyaer.net.cookie);
//		util.showBaseInfo(str_url);
		Thread.sleep(3000);
		HttpHelper httpHelper = new HttpHelper();
		System.out.println(httpHelper.sendRequest(str_url,com.tyaer.net.cookie));*/
    }

}
