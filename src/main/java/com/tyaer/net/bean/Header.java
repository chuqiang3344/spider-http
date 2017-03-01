package com.tyaer.net.bean;

import org.apache.http.client.methods.HttpRequestBase;

import java.lang.reflect.Field;

/**
 * Created by Twin on 2016/11/21.
 */
public class Header {
    protected String user_Agent;

    private String Accept = "*/*";
    private String Accept_Language = "zh-CN,zh;q=0.8";
    private String Cache_Control = "no-cache";
    private String Pragma = "no-cache";
    private String Connection = "Keep-Alive";

    private String Cookie ;
    private String referer;
    private String Host;

    public String getUser_Agent() {
        return user_Agent;
    }

    public void setUser_Agent(String user_Agent) {
        this.user_Agent = user_Agent;
    }

    public String getAccept() {
        return Accept;
    }

    public void setAccept(String accept) {
        Accept = accept;
    }

    public String getAccept_Language() {
        return Accept_Language;
    }

    public void setAccept_Language(String accept_Language) {
        Accept_Language = accept_Language;
    }

    public String getCache_Control() {
        return Cache_Control;
    }

    public void setCache_Control(String cache_Control) {
        Cache_Control = cache_Control;
    }

    public String getPragma() {
        return Pragma;
    }

    public void setPragma(String pragma) {
        Pragma = pragma;
    }

    public String getConnection() {
        return Connection;
    }

    public void setConnection(String connection) {
        Connection = connection;
    }

    public String getCookie() {
        return Cookie;
    }

    public void setCookie(String cookie) {
        Cookie = cookie;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public void setHttpHeader(HttpRequestBase httpRequestBase){
        for (Field field : this.getClass().getFields()) {
            try {
                String name = field.getName().replace("_","-");
                String value = (String) field.get(this.getClass());
                httpRequestBase.setHeader(name,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
