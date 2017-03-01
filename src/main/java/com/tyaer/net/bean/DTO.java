package com.tyaer.net.bean;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Twin on 2016/12/8.
 */
public class DTO {

    public static int MAX_TOTAL_CONNECTIONS = 100;
    public static int MAX_ROUTE_CONNECTIONS = 100;
    public static int CONNECTION_TIMEOUT = 5000;
    public static int SOCKET_TIMEOUT = 5000;

    static {
        //加载log4j的配置文件
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(DTO.class.getClassLoader().getResourceAsStream("http.properties"));
            properties.load(in);
            MAX_TOTAL_CONNECTIONS = Integer.parseInt(properties.getProperty("MAX_TOTAL_CONNECTIONS"));
            MAX_ROUTE_CONNECTIONS = Integer.parseInt(properties.getProperty("MAX_ROUTE_CONNECTIONS"));
            CONNECTION_TIMEOUT =Integer.parseInt(properties.getProperty("CONNECTION_TIMEOUT"));
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
