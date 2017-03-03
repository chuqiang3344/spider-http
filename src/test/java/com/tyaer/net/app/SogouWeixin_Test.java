package com.tyaer.net.app;

import com.tyaer.net.bean.ResponseBean;
import com.tyaer.net.http.HttpHelper;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Twin on 2016/12/8.
 */
public class SogouWeixin_Test {
    private static Logger logger=Logger.getLogger(SogouWeixin_Test.class);

    private static HttpHost httpHost=new HttpHost("svnserver", 808);

    static {
        PropertyConfigurator.configure(SogouWeixin_Test.class.getClassLoader().getResourceAsStream("log4j.properties"));

    }

    public static void main(String[] args) {
//        ResponseBean responseBean = HttpHelper.sendRequest("https://www.google.com.hk/search?q=%E6%91%A9%E6%8B%9C%E5%8D%95%E8%BD%A6&biw=1441&bih=740&source=lnms&tbm=nws&sa=X&ved=0ahUKEwik4suqmPPQAhVLebwKHY_WAaYQ_AUIBygC&bav=on.2,or.&bvm=bv.141320020,d.dGc");
//        ResponseBean responseBean = HttpHelper.sendRequest("https://www.google.com.hk/search?q=%E6%91%A9%E6%8B%9C%E5%8D%95%E8%BD%A6&biw=1441&bih=740&source=lnms&tbm=nws&sa=X&ved=0ahUKEwik4suqmPPQAhVLebwKHY_WAaYQ_AUIBygC&bav=on.2,or.&bvm=bv.141320020,d.dGc",httpHost);
//        ResponseBean responseBean = HttpHelper.sendRequest("http://news.seehua.com/%3Fp%3D233693");
//        ResponseBean responseBean = HttpHelper.sendRequest("http://mp.weixin.qq.com/s?src=3&timestamp=1483431559&ver=1&signature=OXnduH3DKYmsU7OdX-Lyp6hhgzRbSkpxCKB*Oz5s19DdXD4sb1WyBUWRbu9aLmP5jOeBxJQmxZWidbevjbpsPFOncAVom0Z3ye37iD81aiRUGzjo4sbzWXJXPNI4eZYtLJEb0vL3gI983etL8WO8NHIpw9GUKkF3g8438ldu0B4=&uin=Nxzzxczxczxc");
//        ResponseBean responseBean = HttpHelper.sendRequest("http://ent.163.com/photoview/00B50003/621309.html");
//        ResponseBean responseBean = HttpHelper.sendRequest("http://pb.sogou.com/pv.gif?uigs_t=1483950070110&uigs_productid=weixin&type=weixin_search_pc&pagetype=index&wuid=undefined&uigs_uuid=1483950069921410&login=0&uigs_refer=&");
        ResponseBean responseBean = HttpHelper.sendRequest("http://weixin.sogou.com/websearch/weixin/pc/anti_account.jsp?t=1483949585912&signature=Phd0UXLFoMX73BHdNwt*-6NZC8hwSJk62uD*9XtyjSTy4scaNu9AcPAE-deC5u5RmFk5kTcwo9siI5kFz9NHuvofuLb1bAa0sai3Q3P6oKIuY9S3VtcL26Z*y00709QZHuBl3O3AzNk*89tQhbhYvsgvd9*zFsHQadQs4y0J6P0QaBpVn3CrSa2yMCDcaBOODqtzEEYc5w-bpqBBwhSZMp4nibEYbxrni14meOcP9yJl3sqCUv8NvN8yfDgV7J2ze7-HSGym9BSNVMZCNe6npkXEC7Z693INXZQ5umJAudDfL7B*Z4TSANvfz2khv1am3Hkw7YcKLImm332Zx7bgnZS25r62efPVUAdRl42-plNRLkITpeoHK4RTxsL3y**xzlb23XYn8*vvp8kQXbtDcBjDZQH9-4xI4UL6D*ABBzuNMZMF-USRnrqSyGWyrR9GIjNzNO*0eUcMEOhRthTSyBGmGDPEVBP3aqKJ9LZjNEReJ5TNsUS2wam9WziDqD-jaxfKK6DxOorgpYNX7*jGw3TyB6GqRpRpi5Nr*bqWSEXd4YenIytjIAACvJC57ywttONqL8VAaKaTAlMaa9wifCRjBBVcNDANaW1v9kdDQlEXBwLeOtO8ci471C*97KJwp2At7SkkSTP4lvp5ecpqH7UMe4x1iBBD8KMvz1wqXoNxv4jqrHAc4YJ7d6D9SXbpjfoAW1xjx9mWa*d0N09NRa*F9aCyd5VybWXbYIz8ZRRCR5J7hoXOS4LxdXhctOGcZK*dAsgrxT6PA6bpNAKbN3p6cHMGjK1DXITQ0gDkgig=");
        out(responseBean);
    }

    public static void out(ResponseBean responseBean){
        logger.info("result");
        System.out.println(responseBean.getUrl());
        System.out.println(responseBean.getStatusCode());
        System.out.println(responseBean.getRawText());
    }

    private final String detectProxyUrl = "http://ip.chinaz.com/getip.aspx";
    @Test
    public void proxy_test(){
        String url="http://weixin.sogou.com/weixin?type=2&query=%E6%91%A9%E6%8B%9C&ie=utf8&_sug_=y&_sug_type_=&w=01015002&oq=mob&ri=0&sourceid=sugg&stj=0%3B0%3B0%3B0&stj2=0&stj0=0&stj1=0&hp=36&hp1=&sut=1888&sst0=1482283952313&lkt=4%2C1482283950258%2C1482283952210";

//        String url="http://news.baidu.com/ns?cl=2&rn=20&tn=news&word=%E6%91%A9%E6%8B%9C";
//        String url=detectProxyUrl;
//        HttpHost httpHost = new HttpHost("", 1);
//        HttpHost httpHost = new HttpHost("svnserver", 808);
        HttpHost httpHost = new HttpHost("120.52.72.59", 80);
//        HttpHost httpHost = new HttpHost("117.169.86.189", 80);
//        HttpHost httpHost = new HttpHost("120.27.113.72", 8888);
//        HttpHost httpHost = new HttpHost("47.90.74.111", 8088);
//        HttpHost httpHost = new HttpHost("117.169.86.143", 80);
//        HttpHost httpHost = new HttpHost("27.219.45.44", 8998);
        ResponseBean responseBean1 = HttpHelper.sendRequest(detectProxyUrl,httpHost);
        out(responseBean1);
        ResponseBean responseBean = HttpHelper.sendRequest(url,httpHost);
        out(responseBean);

    }

    @Test
    public void google(){

        String url="http://weixin.sogou.com/weixin?type=2&query=%E6%91%A9%E6%8B%9C&ie=utf8&_sug_=y&_sug_type_=&w=01015002&oq=mob&ri=0&sourceid=sugg&stj=0%3B0%3B0%3B0&stj2=0&stj0=0&stj1=0&hp=36&hp1=&sut=1888&sst0=1482283952313&lkt=4%2C1482283950258%2C1482283952210";
        HttpHost httpHost = new HttpHost("svnserver", 808);
        ResponseBean responseBean1 = HttpHelper.sendRequest(url,httpHost);
        out(responseBean1);
    }

    @Test
    public void sogou(){
        List<String> strings = null;
        try {
            strings = FileUtils.readLines(new File("./file/recentTask.log"), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url="http://weixin.sogou.com/weixin?usip=null&query=(keyword)&from=tool&ft=&tsn=1&et=&interation=null&type=2&wxid=&page=1&ie=utf8";
//        String url="http://weixin.sogou.com/weixinwap?usip=null&query=(keyword)&from=tool&ft=&tsn=1&et=&interation=null&type=2&wxid=&page=1&ie=utf8";
//        String url="http://weixin.sogou.com/weixinwap?page=1&_rtype=json&ie=utf8&type=2&t=1483062320131&query=(keyword)&pg=webSearchList&_sug_=n&_sug_type_=&tsn=1";



        /**
         * Cookie池
         */

        String cookie1 = "ABTEST=0|1484095795|v1;IPLOC=CN4403;PHPSESSID=q02o0pgool2slajfd36kus8mf4;SUID=FBFD0FB7771A910A0000000058758133;SUIR=1484095796;SUV=00525A9FB70FFDFB58758134637CD963;SNUID=";
//        String cookie1 = "ABTEST=0|1486083111|v1;IPLOC=CN4403;PHPSESSID=7gu48pf9774flcr5u2ipial4i5;SUID=BFF80FB7721A910A000000005893D427;SUIR=1486083111;SUV=004D7A59B70FF8BF5893D427E5708572;SNUID=";
//        String cookie2= cookie1+SogouId.post();
        String cookie2= cookie1+"80C73088403A78D5D1BD494A404809B6";

        int n=1;
        while (true){
            String keyword = strings.get(getRandom(0, strings.size()));
            String searchUrl=url.replace("(keyword)", keyword).trim();
//            System.out.println(searchUrl);
//            String com.tyaer.net.cookie ="";
            ResponseBean responseBean1 = HttpHelper.sendRequest(searchUrl, cookie2);
//        out(responseBean1);
            String html = responseBean1.getRawText();
//            System.out.println(html);
            Document document = Jsoup.parse(html);
            String title = document.select("title").text();
            if("搜狗搜索".equals(title)){
                String text = document.select("div.content-box>p.p2").text();
                logger.error(text);
//                cookie2= cookie1+SogouId.post();
                System.exit(0);
            }else{
                String xpath = "div#main>div.news-box>ul.news-list>li>div.txt-box";
                int size = document.select(xpath).size();
                logger.error(title+" "+n+" "+size);
//                logger.error(keyword+" "+n);
                n++;
            }
            try {
                int random = 0;//148
//                int random = 100;//148
//                int random = getRandom(5, 10)*1000;//1184372  164 248 39884 230 36805
//                int random = getRandom(10, 20)*1000;//586681、43
//                int random = getRandom(10, 20)*1000;//800000
//                int random = getRandom(15, 30)*1000; //2080717
//                int random = getRandom(15, 30)*1000; //2080717
//                int random = getRandom(15, 30)*1000; //1293567 60
//                int random = getRandom(15, 30)*1000; //1639950 79
//                int random = getRandom(15, 30)*1000; //2623139 117
//                int random = getRandom(30, 60)*1000; //3617918 83

                Thread.sleep(random);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据指定最小值与最大值生成随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
//		int s = random.nextInt(max);
        return s;
    }


    @Test
    public void random(){
        for (int i = 0; i < 1000; i++) {
            System.out.println(getRandom(0, 5));
        }
    }

}
