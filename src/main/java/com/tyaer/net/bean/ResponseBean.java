package com.tyaer.net.bean;

/**
 * @author Twin
 */
public class ResponseBean {
    private RequestBean request = new RequestBean();
    /**
     * page对应url
     */
    private String url;
    /**
     * 请求响应时间
     */
    private long ping;
    /**
     * 请求返回码
     */
    private int statusCode;
    /**
     * 字符编码
     */
    private String charset;


    /**
     * 页面原始文本
     */
    private String rawText;

    public RequestBean getRequest() {
        return request;
    }

    public void setRequest(RequestBean request) {
        this.request = request;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public long getPing() {
        return ping;
    }

    public void setPing(long ping) {
        this.ping = ping;
    }

    /**
     * get url of current page
     *
     * @return url of current page
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRawText() {
        return rawText;
    }

    public ResponseBean setRawText(String rawText) {
        this.rawText = rawText;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "request=" + request +
                ", url='" + url + '\'' +
                ", ping=" + ping +
                ", statusCode=" + statusCode +
                ", charset='" + charset + '\'' +
                ", rawText='" + rawText + '\'' +
                '}';
    }

    public void out() {
        System.err.println("================================");
        System.out.println(this.getUrl());
        String rawText = this.getRawText();
        System.out.println(this.getStatusCode());
        System.out.println("ping" + this.getPing());
        System.out.println(rawText.getBytes().length);
        System.out.println(rawText);
    }
}
