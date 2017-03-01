package com.tyaer.net.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Twin on 2016/12/19.
 */
public class UrlUtils {
    public static void main(String[] args) {
//        String url="http://weixin.sogou.com/weixin?query=%E6%91%A9%E6%8B%9C&_sug_type_=&_sug_=n&type=2&page=2&ie=utf8";
        String url="http://weixin.sogou.com/weixinwap?page=1&_rtype=json&ie=utf8&type=2&t=1483061392733&query=%E6%91%A9%E6%8B%9C&pg=webSearchList&_sug_=y&_sug_type_=&tsn=1&";
        analyzeParameters(url);
    }

    /**
     * 分析url参数
     * @param url
     * @return
     */
    public static Map<String, String> analyzeParameters(String url) {
        System.out.println(url);
        HashMap<String, String> hashMap = new HashMap<>();
        String[] split = url.split("\\?");
        if (split.length > 1) {
            String parameters_str = split[1];
            String[] parameters = parameters_str.split("&");
            for (String parameter : parameters) {
                String[] split1 = parameter.split("=");
                if(split1.length>1){
                    hashMap.put(split1[0], split1[1]);
                }
            }
        }
        System.out.println(hashMap);
        return hashMap;
    }

    @Test
    public void analyzeCookies_Test() {
        String cookies="ABTEST=5|1482203525|v1; IPLOC=CN4403; SUID=A1C110B7721A910A000000005858A185; SUID=7AFE0FB71E20910A000000005858A186; SUV=00576323B70FFEBF5858A186C04CB255; weixinIndexVisited=1; PHPSESSID=3q2kl6qcpas0qevj8m40mj2e76; SUIR=1482997507; SNUID=3807F64EF8FCBD3EBD864CFFF9A377FE; JSESSIONID=aaasujALLhsu4gdGrs7Kv; sct=2; seccodeErrorCount=1|Thu, 29 Dec 2016 11:11:30 GMT; seccodeRight=success; successCount=2|Thu, 29 Dec 2016 11:08:01 GMT; refresh=1";
        Map<String, String> stringStringMap = analyzeCookies(cookies);
        System.out.println(stringStringMap);
    }

    /**
     * 分析cookies参数
     * @param cookies
     * @return
     */
    public static Map<String, String> analyzeCookies(String cookies) {
        HashMap<String, String> hashMap = new HashMap<>();
        System.out.println(cookies);
        String[] split = cookies.split(";");
        System.out.println();
        for (String cookie : split) {
            System.out.println(cookie);
            String[] kv = cookie.split("=");
            if(kv.length>1){
                hashMap.put(kv[0],kv[1]);
            }
        }
        return hashMap;
    }

}
