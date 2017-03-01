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
//            String com.tyaer.net.cookie = "ABTEST=0|1484094856|v1; IPLOC=CN4403; SUID=FBFD0FB7771A910A0000000058757D88; SUID=FBFD0FB73320910A0000000058757D88; weixinIndexVisited=1; SUV=00D77351B70FFDFB58757D89EF812102; JSESSIONID=aaab71Lol8ZgFbsvMx7Kv";
//            String com.tyaer.net.cookie = "ABTEST=0|1484095097|v1;IPLOC=CN4403;PHPSESSID=9u211oducqbn1ma0ugegeg3vf6;SUID=FBFD0FB7771A910A0000000058757E79;SUIR=1484095097;SUV=00AA1781B70FFDFB58757E79C6480116; SNUID=16C863C37471365476ECD819748D5CB4;";
//            String com.tyaer.net.cookie = "ABTEST=0|1484094856|v1; IPLOC=CN4403; SUID=FBFD0FB7771A910A0000000058757D88; SUID=FBFD0FB73320910A0000000058757D88; weixinIndexVisited=1; SUV=00D77351B70FFDFB58757D89EF812102; JSESSIONID=aaab71Lol8ZgFbsvMx7Kv; PHPSESSID=dnrthnbepcbpusv96ien2j16e4; SUIR=1484095202; SNUID=F7F104BC0B0E4ECB065D686F0C9DC132; refresh=1";
        String cookie1 = "ABTEST=0|1484095795|v1;IPLOC=CN4403;PHPSESSID=q02o0pgool2slajfd36kus8mf4;SUID=FBFD0FB7771A910A0000000058758133;SUIR=1484095796;SUV=00525A9FB70FFDFB58758134637CD963;SNUID=";
//        String cookie1 = "ABTEST=0|1486083111|v1;IPLOC=CN4403;PHPSESSID=7gu48pf9774flcr5u2ipial4i5;SUID=BFF80FB7721A910A000000005893D427;SUIR=1486083111;SUV=004D7A59B70FF8BF5893D427E5708572;SNUID=";
//        String cookie2= cookie1+SogouId.post();
        String cookie2= cookie1+"2462952C9B9FDC0C9C1786EA9B8D9A76";
//        String cookie2= cookie1+"919466DD6A6C2CA328F9CCD06AB09994";
//        String cookie2= cookie1+"E0E114AC1C1E59D25992026A1C034ABC";
//        String cookie2= cookie1+"2324D66ED8DC9C1054177F4ED9087772";
//        String cookie2= cookie1;


//            String com.tyaer.net.cookie = "ABTEST=7|1484045384|v1;IPLOC=CN4403;SUID=1BFE0FB7721A910A000000005874BC48;SUV=00C2178FB70FFE1B5874BC49B2C49260;  SNUID=16C863C37471365476ECD819748D5CB4;";
//            String com.tyaer.net.cookie = "ABTEST=7|1484045384|v1;IPLOC=CN4403;SUID=1BFE0FB7721A910A000000005874BC48;SUV=00C2178FB70FFE1B5874BC49B2C49260;  SNUID=AA4FBE07B1B4F40BE97692E4B100EE09;";
//            String com.tyaer.net.cookie = "ABTEST=7|1484046472|v1;IPLOC=CN4403;SUID=1BFE0FB7721A910A000000005874C088;SUV=008F7350B70FFE1B5874C089C1E9D312; SNUID=26C3338B3C3878865259F1B13D17C703;";
//            String com.tyaer.net.cookie = "SNUID=16C863C37471365476ECD819748D5CB4; JSESSIONID=aaa2XKmlFvE4G_cKFt7Kv;";
//            String com.tyaer.net.cookie = "ABTEST=8|1484047028|v1;IPLOC=CN4403;PHPSESSID=svgbap8eu6ml1n42bnch0j5b94;SUID=1BFE0FB7721A910A000000005874C2B4;SUIR=1484047028;SUV=00C2178FB70FFE1B5874C2B46A37F666;SNUID=4DCDDC15CECB8B75573694D6CFB2D069";
//            String com.tyaer.net.cookie = "ABTEST=8|1484047028|v1;IPLOC=CN4403;PHPSESSID=svgbap8eu6ml1n42bnch0j5b94;SUID=1BFE0FB7721A910A000000005874C2B4;SUIR=1484047028;SUV=00C2178FB70FFE1B5874C2B46A37F666;SNUID=991909C11C1E59A7A949BA571C5C0B10";
//            String com.tyaer.net.cookie = "ABTEST=8|1484047028|v1;IPLOC=CN4403;PHPSESSID=svgbap8eu6ml1n42bnch0j5b94;SUID=1BFE0FB7721A910A000000005874C2B4;SUIR=1484047028;SUV=00C2178FB70FFE1B5874C2B46A37F666;SNUID=FF7C6FA77E7B38C5B12E7C077EAF4D9A";
//            String com.tyaer.net.cookie = "ABTEST=0|1484047206|v1; IPLOC=CN4403; SUID=820212DA771A910A000000005874C366; SUID=820212DA3020910A000000005874C366; weixinIndexVisited=1; SUV=004D7A59DA1202825874C367AE56F976; JSESSIONID=aaanVV8I4_uhNdsMux7Kv";

//            String com.tyaer.net.cookie = "ABTEST=5|1482203525|v1; IPLOC=CN4403; SUID=A1C110B7721A910A000000005858A185; SUID=7AFE0FB71E20910A000000005858A186; SUV=00576323B70FFEBF5858A186C04CB255; weixinIndexVisited=1; PHPSESSID=3q2kl6qcpas0qevj8m40mj2e76; SUIR=1482997507; SNUID=3807F64EF8FCBD3EBD864CFFF9A377FE; JSESSIONID=aaasujALLhsu4gdGrs7Kv; sct=2; successCount=10|Thu, 29 Dec 2016 11:08:01 GMT; seccodeErrorCount=1|Thu, 29 Dec 2016 11:11:30 GMT";
//            String com.tyaer.net.cookie = "SUID=92FA0FB72F20910A000000005864B60B; SUV=004D7A59B70FFA925865B035A4F97018; ABTEST=7|1483059258|v1; SUIR=1483059258; usid=tXbzQqh6js5M5HXb; weixinIndexVisited=1; SNUID=16C863C37471365476ECD819748D5CB4; JSESSIONID=aaa2XKmlFvE4G_cKFt7Kv; sct=8; IPLOC=CN4400";
//            String com.tyaer.net.cookie = "ABTEST=3|1483513707|v1; weixinIndexVisited=1; JSESSIONID=aaaIyEg9ge5fNbLdOt7Kv; ABTEST=3|1483513707|v1; SUID=5E99CADC771A910A00000000586C9F6B; weixinIndexVisited=1; SUV=00C2178FDCCA995E586C9F6BD3649369; SNUID=A4633325FAFFBFC68DFC3673FA45CE1C; usid=nuWVOwcMgBGAiXsA; IPLOC=CN4301; wapsogou_qq_nickname=; JSESSIONID=aaaIyEg9ge5fNbLdOt7Kv";
//            String com.tyaer.net.cookie = "SUV=00F7AD2A3A3CA85458368E953FA39463; wuid=AAH56LpLFQAAAAqRIjEbgAUA1wA=; ABTEST=0|1482820855|v1; SUID=710010B7721A910A0000000058620CF7; weixinIndexVisited=1; SUID=8C0310B77C20940A0000000058620D07; usid=zgVinCM4btQ6gDWI; create_date=27; clientId=17fc811c-ba71-4380-9018-7605bd8151a5; user_id=woluge; woa_mb=ODYxODY3MDkyNjU2OA%3D%3D; counter=1; SNUID=4BBC4EF64247077E26F80121428A0E9A; IPLOC=CN4403; wapsogou_qq_nickname=; JSESSIONID=aaajXEzgLbVbch86Nt7Kv";
//            String com.tyaer.net.cookie = "ABTEST=8|1482203438|v1; SUV=00F901F1B70FFE7A5858A12EE4DBB289; IPLOC=CN4403; SUID=7AFE0FB7721A910A000000005858A12E; SUIR=1482203438; SUID=AAF40FB71E20910A00000000585C8279; weixinIndexVisited=1; SNUID=5D03F74FF8FDBCBD1AFDF0ECF8FA3E52; sct=7; PHPSESSID=dmjioq435eblpah154ihc13k93; seccodeRight=success; successCount=4|Mon, 26 Dec 2016 01:49:26 GMT; JSESSIONID=aaatYvMc1xW6k4C58PBKv";
//            String com.tyaer.net.cookie = "ABTEST=5|1482372333|v1; SUID=A5C8403A721A910A00000000585B34ED; SUID=A5C8403A280B940A00000000585B34EF; SUIR=8CE169102A2F6E1F9E4990D62AF21F8F; SUV=005E5558DA1203E3585B35C7457BF499; weixinIndexVisited=1; pgv_pvi=5245996032; sortcookie=1; ld=5i5SsZllll2Yg2SflllllVPtfWolllllHZ@CdkllllYlllllVZlll5@@@@@@@@@@; SNUID=C07B823A8D88CA08CFCE645E8E618263; IPLOC=CN4403; JSESSIONID=aaaMGRlH98Y5ufaHCp7Kv; sct=19";
//            String com.tyaer.net.cookie = "IPLOC=CN4403; SUID=92FA0FB72F20910A000000005864B60B; SUV=004D7A59B70FFA925865B035A4F97018; ABTEST=7|1483059258|v1; SUIR=1483059258; usid=tXbzQqh6js5M5HXb; weixinIndexVisited=1; SNUID=16C863C37471365476ECD819748D5CB4; JSESSIONID=aaaV9Oz8YDu38bpa2s7Kv; sct=3";

//            String com.tyaer.net.cookie = "ABTEST=5|1482203525|v1; IPLOC=CN4403; SUID=A1C110B7721A910A000000005858A185; SUID=7AFE0FB71E20910A000000005858A186; SUV=0057632
//
// 3B70FFEBF5858A186C04CB255; weixinIndexVisited=1; PHPSESSID=3q2kl6qcpas0qevj8m40mj2e76; SUIR=1482997507; SNUID=3807F64EF8FCBD3EBD864CFFF9A377FE; JSESSIONID=aaasujALLhsu4gdGrs7Kv; sct=2; seccodeErrorCount=1|Thu, 29 Dec 2016 11:26:16 GMT";
//            String com.tyaer.net.cookie = "ABTEST=8|1483010700|v1;IPLOC=CN4403;SUID=71C110B7771A910A000000005864F28C;SUID=7AFE0FB71E20910A000000005858A186; SUV=00576323B70FFEBF5858A186C04CB255; weixinIndexVisited=1; PHPSESSID=3q2kl6qcpas0qevj8m40mj2e76; SUIR=1482997507; SNUID=3807F64EF8FCBD3EBD864CFFF9A377FE; JSESSIONID=aaasujALLhsu4gdGrs7Kv; sct=2; seccodeErrorCount=2|Thu, 29 Dec 2016 11:26:16 GMT; seccodeErrorCount=1|Thu, 29 Dec 2016 11:11:30 GMT";

//            String com.tyaer.net.cookie = "ABTEST=5|1482203525|v1; IPLOC=CN4403; SUID=A1C110B7721A910A000000005858A185; SUID=7AFE0FB71E20910A000000005858A186; SUV=00576323B70FFEBF5858A186C04CB255; weixinIndexVisited=1; PHPSESSID=3q2kl6qcpas0qevj8m40mj2e76; SUIR=1482997507; SNUID=3807F64EF8FCBD3EBD864CFFF9A377FE; JSESSIONID=aaasujALLhsu4gdGrs7Kv; sct=2; seccodeRight=success; successCount=10|Thu, 29 Dec 2016 11:31:43 GMT; refresh=1";
//            String com.tyaer.net.cookie = "IPLOC=CN4403; SUID=92FA0FB72F20910A000000005864B60B; SUV=004D7A59B70FFA925865B035A4F97018; ABTEST=7|1483059258|v1; PHPSESSID=kmcmc3tjjqneog8oq6qskjnej7; SUIR=1483059258; SNUID=3C55A118AEABEA7B0A4DF61CAF73509E; usid=tXbzQqh6js5M5HXb; weixinIndexVisited=1; JSESSIONID=aaaU8mJnZMDiq9aP1q7Kv; sct=2; wapsogou_qq_nickname=";
//            String com.tyaer.net.cookie = "IPLOC=CN4403; SUID=76C410B73220910A00000000585B8216; SUV=007810FCB710C476585B8217E5A6A082; pgv_pvi=2490095616; ABTEST=0|1482392210|v1; SNUID=2E6C7CB46E6B2A4AA7720B396F138321; sct=8; ld=S0GyDyllll2YQcwQlllllVP$D2UlllllHeR9Nkllll9llllllZlll5@@@@@@@@@@; LSTMV=189%2C29; LCLKINT=1552; JSESSIONID=aaarTNQX1R7A-4eDMu7Kv";
//            String com.tyaer.net.cookie = "ABTEST=5|1482203525|v1; IPLOC=CN4403; SUID=A1C110B7721A910A000000005858A185; SUID=7AFE0FB71E20910A000000005858A186; SUV=00576323B70FFEBF5858A186C04CB255; weixinIndexVisited=1; PHPSESSID=3q2kl6qcpas0qevj8m40mj2e76; SUIR=1482997507; SNUID=3807F64EF8FCBD3EBD864CFFF9A377FE; JSESSIONID=aaasujALLhsu4gdGrs7Kv; sct=2; seccodeErrorCount=1|Thu, 29 Dec 2016 11:34:38 GMT; seccodeRight=success; successCount=2|Thu, 29 Dec 2016 11:31:43 GMT; refresh=1";

//            String com.tyaer.net.cookie = "IPLOC=CN4403; SUID=76C410B73220910A00000000585B8216; SUV=007810FCB710C476585B8217E5A6A082; pgv_pvi=2490095616; ABTEST=0|1482392210|v1; SNUID=2E6C7CB46E6B2A4AA7720B396F138321; sct=8; ld=S0GyDyllll2YQcwQlllllVP$D2UlllllHeR9Nkllll9llllllZlll5@@@@@@@@@@; LSTMV=189%2C29; LCLKINT=1552; JSESSIONID=aaarTNQX1R7A-4eDMu7Kv";

//            String com.tyaer.net.cookie = "ABTEST=5|1482203525|v1; IPLOC=CN4403; SUID=A1C110B7721A910A000000005858A185; SUID=7AFE0FB71E20910A000000005858A186; SUV=00576323B70FFEBF5858A186C04CB255; weixinIndexVisited=1; PHPSESSID=3q2kl6qcpas0qevj8m40mj2e76; SUIR=1482997507; SNUID=3807F64EF8FCBD3EBD864CFFF9A377FE; seccodeRight=success; successCount=1|Thu, 29 Dec 2016 07:50:16 GMT; JSESSIONID=aaasujALLhsu4gdGrs7Kv";
//            String com.tyaer.net.cookie = "ABTEST=8|1482203438|v1; SUV=00F901F1B70FFE7A5858A12EE4DBB289; IPLOC=CN4403; SUID=7AFE0FB7721A910A000000005858A12E; SUIR=1482203438; SUID=AAF40FB71E20910A00000000585C8279; weixinIndexVisited=1; PHPSESSID=q9c6st53c8ejh0mjchqgr9amo7; SNUID=5D03F74FF8FDBCBD1AFDF0ECF8FA3E52; JSESSIONID=aaavqqUCcXsbcezD0OBKv; sct=4; seccodeErrorCount=1|Fri, 23 Dec 2016 09:09:16 GMT; seccodeRight=success; successCount=4|Fri, 23 Dec 2016 09:09:20 GMT; refresh=1";


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
