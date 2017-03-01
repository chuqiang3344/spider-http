package com.tyaer.net.bean;

import org.apache.http.HttpHost;

import java.util.HashMap;

/**
 * Created by Twin on 2016/9/22.
 */
public class RequestBean {
    private String url;
    private String type="get";

    private Header header=new Header();

    private HttpHost httpHost;

    private int retriesNum = 3;

    private HashMap<String,String> parameter;

    public HashMap<String, String> getParameter() {
        return parameter;
    }

    public void setParameter(HashMap<String, String> parameter) {
        this.parameter = parameter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HttpHost getHttpHost() {
        return httpHost;
    }

    public void setHttpHost(HttpHost httpHost) {
        this.httpHost = httpHost;
    }

    public int getRetriesNum() {
        return retriesNum;
    }

    public void setRetriesNum(int retriesNum) {
        this.retriesNum = retriesNum;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setUserAgent(String userAgent) {
        this.header.user_Agent = userAgent;
    }

    public String getCookie() {
        return header.getCookie();
    }

    @Override
    public String toString() {
        return "RequestBean{" +
                "url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", header=" + header +
                ", httpHost=" + httpHost +
                ", retriesNum=" + retriesNum +
                ", parameter=" + parameter +
                '}';
    }
}
