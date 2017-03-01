package com.tyaer.net.bean;

/**
 * @ClassName: HttpMethodType
 * @Description: Http协议的method，如get，post，input。
 * @author: panyinghua@fangdd.com
 * @date: 2015年3月12日 下午5:01:31
 */
public enum HttpMethodType {

    GET(0, "GET"),
    POST(1, "POST"),
    HEAD(2, "HEAD"),
    PUT(3, "PUT"),
    DELETE(4, "DELETE"),
    TRACE(5, "TRACE"),
    OPTIONS(6, "OPTIONS");


    /**
     * 对应的http method index
     */
    private int methodIndex;
    /**
     * http method nickname
     */
    private String methodName;

    HttpMethodType(int index, String name) {
        this.methodIndex = index;
        this.methodName = name;
    }

    public int getMethodIndex() {
        return this.methodIndex;
    }

    public String getMethodName() {
        return this.methodName;
    }

}
