package cookie;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tyaer.net.utils.HttpUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.*;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Twin on 2017/1/11.
 */
public class SogouId {
    private static CloseableHttpClient client = HttpClients.createDefault();


    public static void main(String[] args) {
        String post = post();
        System.out.println(post);
    }

    public static String post(){
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("c","r82rb7");
        paramsMap.put("r","%2Fweixin%3Ftype%3D2%26query%3D%E8%87%AA%E8%A1%8C%E8%BD%A6%26ie%3Dutf8%26_sug_%3Dy%26_sug_type_%3D");
        paramsMap.put("r","5");
        String loginUrl = "http://weixin.sogou.com/antispider/thank.php";
        HttpPost post = new HttpPost(loginUrl);
        post.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build());
        post.setHeader("User-Agent", "Mozilla/5.0(Windows;U;WindowsNT6.1;rv:2.2)Gecko/20110201");
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        post.setHeader("Referer", "http://weixin.sogou.com/antispider/?from=%2fweixin%3Ftype%3d2%26query%3d%E8%87%AA%E8%A1%8C%E8%BD%A6%26ie%3dutf8%26_sug_%3dy%26_sug_type_%3d");
        post.setHeader("Host", "weixin.sogou.com");
//        post.setHeader("Origin", "http://tieba.baidu.com");
//        String cookie="BAIDUID=36BED8CAD0D134B4A713ED4D0F0B8119:FG=1;BDUSS=BvYlFBSmc1a1JqUUFXamR3NGxVMFNubWh2Y3VNb3NFYnpoVzRDQjRXWDRHVkZZSVFBQUFBJCQAAAAAAAAAAAEAAACd8KMIY2h1cWlhbmczMzQ0AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPiMKVj4jClYNV;BIDUPSID=36BED8CAD0D134B4A713ED4D0F0B8119;PSTM=1479118073;BDRCVFR[laz3IxV61qm]=mbxnW11j9Dfmh7GuZR8mvqV;BD_HOME=1;H_PS_PSSID=17942;__bsi=13197132025009865138_00_14_N_N_97_0303_C02F_N_N_N_0;BD_UPN=1123314251;";
//        String cookie="BAIDUID=08B969F39AABC5AF0CDFF942237DB6E1:FG=1; BIDUPSID=08B969F39AABC5AF0CDFF942237DB6E1; PSTM=1479108894; BDRCVFR[EzYDMnK0Xs6]=mk3SLVN4HKm; BDRCVFR[laz3IxV61qm]=mk3SLVN4HKm; TIEBAUID=512142465a71521c730a710a; TIEBA_USERTYPE=9d2ff1ebe93dd527e01c2f37; bdshare_firstime=1479118177616; Hm_lvt_287705c8d9e2073d13275b18dbd746dc=1479118179; Hm_lpvt_287705c8d9e2073d13275b18dbd746dc=1479118179; H_PS_PSSID=1450_21422_17948_21080_18559_21455_21409_21378_21526_21190_21306; BDUSS=R2UU1YVzVKTW1zaW52eFBlenhVbVRlLVlLVVZJWGpEakRmN3lCeVpQSUNJMUZZSVFBQUFBJCQAAAAAAAAAAAEAAACWZkiOOTZhMzc2NAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKWKVgClilYMF; wise_device=0; LONGID=2387109526";
//        String cookie="ABTEST=0|1484097170|v1; IPLOC=CN4403; SUID=FBFD0FB7771A910A0000000058758692; SUID=FBFD0FB73320910A0000000058758693; weixinIndexVisited=1; SUV=0048178AB70FFDFB58758693A293E694; JSESSIONID=aaaPQEfJP1gZHh4NNx7Kv; PHPSESSID=vcc6af770cq97jsevfkoa2btu2; SUIR=1484097221";
//        String cookie="ABTEST=0|1484097170|v1; IPLOC=CN4403; SUID=FBFD0FB7771A910A0000000058758692; SUID=FBFD0FB73320910A0000000058758693; weixinIndexVisited=1; SUV=0048178AB70FFDFB58758693A293E694; JSESSIONID=aaaPQEfJP1gZHh4NNx7Kv; PHPSESSID=vcc6af770cq97jsevfkoa2btu2; SUIR=1484097221";
        String cookie="ABTEST=0|1484095795|v1;IPLOC=CN4403;PHPSESSID=vcc6af770cq97jsevfkoa2btu2;SUID=FBFD0FB7771A910A0000000058758133;SUIR=1484095796;SUV=00525A9FB70FFDFB58758134637CD963;";
        post.setHeader("Cookie",cookie);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : paramsMap.keySet()) {
            params.add(new BasicNameValuePair(key, paramsMap.get(key)));
        }
        String result = "";
        try {
            UrlEncodedFormEntity eparams = new UrlEncodedFormEntity(params, "utf-8");
            post.setEntity(eparams);
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            result=IOUtils.toString(entity.getContent(),"utf-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
//        System.out.println("cookie:" + result);
        JSONObject jsonObject = JSON.parseObject(result);
        String id = jsonObject.getString("id");
        return id;
    }


}
