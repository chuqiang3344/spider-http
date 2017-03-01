package com.tyaer.net.config;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Twin on 2017/3/1.
 */
public class HttpConfig {

    public static int MAX_TOTAL_CONNECTIONS = 100;
    public static int MAX_ROUTE_CONNECTIONS = 100;
    public static int CONNECTION_TIMEOUT = 5000;
    public static int SOCKET_TIMEOUT = 5000;

    static {
        //加载log4j的配置文件
        Properties properties = new Properties();
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(HttpConfig.class.getResourceAsStream("/http.properties"));
            properties.load(in);
            MAX_TOTAL_CONNECTIONS = Integer.parseInt(properties.getProperty("MAX_TOTAL_CONNECTIONS"));
            MAX_ROUTE_CONNECTIONS = Integer.parseInt(properties.getProperty("MAX_ROUTE_CONNECTIONS"));
            CONNECTION_TIMEOUT = Integer.parseInt(properties.getProperty("CONNECTION_TIMEOUT"));
            SOCKET_TIMEOUT = Integer.parseInt(properties.getProperty("SOCKET_TIMEOUT"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            properties.clear();
        }
    }

    public static void main(String[] args) {

    }
}
