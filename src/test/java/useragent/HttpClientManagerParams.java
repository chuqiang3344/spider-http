package useragent;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 请求协议管理
 *
 * @author mg
 */
public class HttpClientManagerParams {
    //http请求协议
    private static String[] userAgent = new String[]{
            "Mozilla/5.0(WindowsNT6.2;Win64;x64)AppleWebKit/537.36(KHTML,likeGecko)Chrome/32.0.1667.0Safari/537.36",
            "Mozilla/5.0(Macintosh;IntelMacOSX10_9_0)AppleWebKit/537.36(KHTML,likeGecko)Chrome/32.0.1664.3Safari/537.36",
            "Mozilla/5.0(Windows;U;WindowsNT6.1;rv:2.2)Gecko/20110201",
            "Mozilla/5.0(Macintosh;U;IntelMacOSX10.5;en-US;rv:1.9.0.1)Gecko/2008070206",
            "Mozilla/5.0(X11;Ubuntu;Linuxx86_64;rv:24.0)Gecko/20100101Firefox/24.0",
            "Mozilla/5.0(compatible;U;ABrowse0.6;Syllable)AppleWebKit/420+(KHTML,likeGecko)",
            "Mozilla/5.0(compatible;MSIE8.0;WindowsNT6.0;Trident/4.0;AcooBrowser1.98.744;.NETCLR3.5.30729)",
            "Mozilla/4.0(compatible;MSIE7.0;WindowsNT6.0;AcooBrowser;GTB5;Mozilla/4.0(compatible;MSIE6.0;WindowsNT5.1;SV1);Maxthon;InfoPath.1;.NETCLR3.5.30729;.NETCLR3.0.30618)",
            "Mozilla/4.0(compatible;MSIE7.0;AmericaOnlineBrowser1.1;WindowsNT5.1;(R11.5);.NETCLR2.0.50727;InfoPath.1)",
            "Mozilla/4.0(compatible;MSIE7.0;AmericaOnlineBrowser1.1;rev1.5;WindowsNT5.1;.NETCLR1.1.4322;.NETCLR2.0.50727)",
            "AmigaVoyager/3.2(AmigaOS/MC680x0)",
            "AmigaVoyager/2.95(compatible;MC680x0;AmigaOS;SV1)",
            "Mozilla/5.0(WindowsNT;U;en)AppleWebKit/525.18.1(KHTML,likeGecko)Version/3.1.1Iris/1.1.7Safari/525.20",
            "Mozilla/4.0(compatible;MSIE8.0;WindowsNT5.2;WOW64;Trident/4.0;uZardWeb/1.0;Server_USA)",
            "Mozilla/5.0(Macintosh;IntelMacOSX10_6_8)AppleWebKit/537.13+(KHTML,likeGecko)Version/5.1.7Safari/534.57.2",
            "Mozilla/5.0(compatible;MSIE9.0;WindowsNT6.0;Trident/5.0;TheWorld)",
            "Mozilla/4.0(compatible;MSIE8.0;WindowsNT6.1;WOW64;Trident/4.0;SLCC2;.NETCLR2.0.50727;.NETCLR3.5.30729;.NETCLR3.0.30729;MediaCenterPC6.0;InfoPath.2;TheWorld)",
            "Mozilla/4.0(compatible;MSIE8.0;WindowsNT6.1;Trident/4.0;GTB6.5;SLCC2;.NETCLR2.0.50727;.NETCLR3.5.30729;.NETCLR3.0.30729;MediaCenterPC6.0;.NET4.0C;TheWorld)",
            "Mozilla/4.0(compatible;MSIE8.0;WindowsNT5.1;Trident/4.0;InfoPath.2;.NETCLR2.0.50727;TheWorld)",
            "Mozilla/4.0(compatible;MSIE8.0;WindowsNT6.0;Trident/4.0;TencentTraveler4.0;Trident/4.0;SLCC1;MediaCenterPC5.0;.NETCLR2.0.50727;.NETCLR3.5.30729;.NETCLR3.0.30618)",
            "Mozilla/4.0(compatible;MSIE8.0;WindowsNT5.1;Trident/4.0;iCafeMedia;TencentTraveler4.0;Mozilla/4.0(compatible;MSIE6.0;WindowsNT5.1;SV1);.NETCLR1.1.4322;.NETCLR2.0.50727;.NETCLR3.0.4506.2152;.NETCLR3.5.30729)",
            "Mozilla/4.0(compatible;MSIE7.0;WindowsNT6.0;QQPinyin686;QQDownload661;GTB6.6;TencentTraveler4.0;SLCC1;.NETCLR2.0.50727;MediaCenterPC5.0;.NETCLR3.0.04506)",
            "Mozilla/6.0(X11;U;Linuxx86_64;en-US;rv:2.9.0.3)Gecko/2009022510FreeBSD/Sunrise/4.0.1/likeSafari",
            "Mozilla/5.0(Macintosh;U;IntelMacOSX10_5_5;ja-jp)AppleWebKit/525.18(KHTML,likeGecko)Sunrise/1.7.5likeSafari/5525.20.1",
            "Sundance/0.9x(Compatible;Windows;U;en-US;)Version/0.9x",
            "Mozilla/5.0(Macintosh;U;IntelMacOSX10_5_6;en-us)AppleWebKit/528.16(KHTML,likeGecko)Stainless/0.5.3Safari/525.20.1",
            "Mozilla/4.0(compatible;MSIE8.0;WindowsNT6.0;Trident/4.0;SLCC1;.NETCLR2.0.50727;MediaCenterPC5.0;.NETCLR3.5.30729;.NETCLR3.0.30618;.NET4.0C;.NET4.0E;Sleipnir/2.9.9)",
            "Mozilla/5.0(Macintosh;U;PPCMacOSX;ja-jp)AppleWebKit/419(KHTML,likeGecko)Shiira/1.2.3Safari/125",
            "Mozilla/5.0(WindowsNT5.2;RW;rv:7.0a1)Gecko/20091211SeaMonkey/9.23a1pre",
            "Opera/12.80(WindowsNT5.1;U;en)Presto/2.10.289Version/12.02",
            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E)",
            "ADmantX Platform Semantic Analyzer - APAC - ADmantX Inc. - www.admantx.com - support@admantx.com",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/601.5.17 (KHTML, like Gecko) Version/9.1 Safari/601.5.17",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729)",
            "Mozilla/5.0 (Windows; U; Windows NT 5.2; zh-CN) AppleWebKit/534.31 (KHTML, like Gecko) Chrome/17.0.558.0 Safari/534.31 baidubrowser/7.5.22.0 (Baidu; P1 4.3)",
            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36",
            "Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv 11.0) like Gecko",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.8.1000 Chrome/30.0.1599.101 Safari/537.36"
    };

    /**
     * 随机获取一个请求头
     */
    public static String getRandomUsersAgent() {
        int index = getRandom(0, userAgent.length);
        return userAgent[index];
    }

    /**
     * 根据指定最小值与最大值生成随机数，最小值为0。则最大值只有max-1
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

    public static void main(String[] args) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < 100000; i++) {
            int random = getRandom(1, 10);
            if (hashMap.get(random) == null) {
                hashMap.put(random, 1);
            } else {
                Integer num = hashMap.get(random);
                num++;
                hashMap.put(random, num);
            }
        }
        System.out.println(hashMap);
    }

    /**
     * 测试获取userAgent
     **/
    @Test
    public void testAgent() throws IOException, InterruptedException {
        int length = HttpClientManagerParams.userAgent.length;
        String url = "http://s.weibo.com/weibo/" + URLEncoder.encode("王宝强", "UTF-8") + "&nodup=1";//"http://www.atool.org/useragent.php"
//		微博
//		String url="http://weibo.com/1026427652/E6QSaFvKQ?from=page_1005051026427652_profile&wvr=6&mod=weibotime&type=comment";
        System.out.println(url);
        Pattern pattern = Pattern.compile("\\$CONFIG\\['bigpipe'\\] = '(.*?)';");
        for (int i = 0; i < length; i++) {
            CloseableHttpClient aDefault = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            String userAgent = HttpClientManagerParams.userAgent[i];
            httpGet.setHeader("User-Agent", userAgent);
//            httpGet.setHeader("Cookie", "");
            CloseableHttpResponse execute = null;
            try {
                execute = aDefault.execute(httpGet);
                String html = IOUtils.toString(execute.getEntity().getContent(), "UTF-8");
                Matcher matcher = pattern.matcher(html);
                if (matcher.find()) {
                    String group = matcher.group(1);
                    if (group.equals("false")) {
                        System.out.println("过时的：" + userAgent);
                        System.out.println(html);
                    } else {
                        System.out.println("正常：" + userAgent);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                EntityUtils.consume(execute.getEntity());
            }
            Thread.sleep(30000);
        }
    }


    /**
     * 测试获取userAgent
     **/
    @Test
    public void testAgent2() throws IOException, InterruptedException {
        String cookie = "YF-V5-G0=69afb7c26160eb8b724e8855d7b705c6; _s_tentry=-; Apache=4397365853656.0835.1484723950063; SINAGLOBAL=4397365853656.0835.1484723950063; ULV=1484723950066:1:1:1:4397365853656.0835.1484723950063:; YF-Page-G0=046bedba5b296357210631460a5bf1d2; SUB=_2AkMvI80sf8NhqwJRmP0RxGPka4V1ww7EieKZfzz3JRMxHRl-yT83qkkptRBxtDWvOJvqp2I1Y85XsOvjpRF2kw..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WhXP5zlV.UkuM3K5Slq3ADu; YF-Ugrow-G0=9642b0b34b4c0d569ed7a372f8823a8e; WBtopGlobal_register_version=c689c52160d0ea3b";
        int length = HttpClientManagerParams.userAgent.length;
//        String url = "http://s.weibo.com/weibo/" + URLEncoder.encode("王宝强", "UTF-8") + "&nodup=1";//"http://www.atool.org/useragent.php"
        String url = "http://weibo.com/p/1004061195242865/info?mod=pedit_more";
//		微博
//		String url="http://weibo.com/1026427652/E6QSaFvKQ?from=page_1005051026427652_profile&wvr=6&mod=weibotime&type=comment";
        System.out.println(url);
        Pattern pattern = Pattern.compile("\\$CONFIG\\['bigpipe'\\] = '(.*?)';");
        for (int i = 0; i < length; i++) {
            CloseableHttpClient aDefault = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            String userAgent = HttpClientManagerParams.userAgent[i];
            httpGet.setHeader("User-Agent", userAgent);
            httpGet.setHeader("Cookie", cookie);
            HttpEntity entity = null;
            try {
                CloseableHttpResponse execute = aDefault.execute(httpGet);
                entity = execute.getEntity();
                String html = IOUtils.toString(entity.getContent(), "UTF-8");
                Document document = Jsoup.parse(html);
                Elements ele_scripts = document.select("script");
                Document doc_Headerv6 = getScriptHtml(ele_scripts, "Pl_Official_Headerv6");
                if (doc_Headerv6 == null) {
                    System.out.println("parseWeiboUser 出现另一种源码");
                    System.out.println(i + " " + userAgent);
                    System.out.println(html);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                EntityUtils.consume(entity);
            }
//            Thread.sleep(3000);
        }
    }

    /**
     * 测试获取userAgent
     **/
    @Test
    public void testAgent3() throws IOException, InterruptedException {
        String cookie = "YF-V5-G0=69afb7c26160eb8b724e8855d7b705c6; _s_tentry=-; Apache=4397365853656.0835.1484723950063; SINAGLOBAL=4397365853656.0835.1484723950063; ULV=1484723950066:1:1:1:4397365853656.0835.1484723950063:; YF-Page-G0=046bedba5b296357210631460a5bf1d2; SUB=_2AkMvI80sf8NhqwJRmP0RxGPka4V1ww7EieKZfzz3JRMxHRl-yT83qkkptRBxtDWvOJvqp2I1Y85XsOvjpRF2kw..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WhXP5zlV.UkuM3K5Slq3ADu; YF-Ugrow-G0=9642b0b34b4c0d569ed7a372f8823a8e; WBtopGlobal_register_version=c689c52160d0ea3b";
        ArrayList<String> list = new ArrayList<>();
        list.add("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
        list.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0); 360Spider");
        list.add("Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
        int length = list.size();
//        String url = "http://s.weibo.com/weibo/" + URLEncoder.encode("王宝强", "UTF-8") + "&nodup=1";//"http://www.atool.org/useragent.php"
        String url = "http://weibo.com/p/1004061195242865/info?mod=pedit_more";
//		微博
//		String url="http://weibo.com/1026427652/E6QSaFvKQ?from=page_1005051026427652_profile&wvr=6&mod=weibotime&type=comment";
        System.out.println(url);
        Pattern pattern = Pattern.compile("\\$CONFIG\\['bigpipe'\\] = '(.*?)';");
        for (int i = 0; i < length; i++) {
            CloseableHttpClient aDefault = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            String userAgent = list.get(i);
            httpGet.setHeader("User-Agent", userAgent);
            httpGet.setHeader("Cookie", cookie);
            HttpEntity entity = null;
            try {
                CloseableHttpResponse execute = aDefault.execute(httpGet);
                entity = execute.getEntity();
                String html = IOUtils.toString(entity.getContent(), "UTF-8");
                Document document = Jsoup.parse(html);
                Elements ele_scripts = document.select("script");
                Document doc_Headerv6 = getScriptHtml(ele_scripts, "Pl_Official_Headerv6");
                if (doc_Headerv6 == null) {
                    System.out.println("parseWeiboUser 出现另一种源码");
                    System.out.println(i + " " + userAgent);
                    System.out.println(html);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                EntityUtils.consume(entity);
            }
            Thread.sleep(3000);
        }
    }

    /**
     * 测试获取userAgent
     **/
    @Test
    public void testAgentOne() throws IOException, InterruptedException {
        String cookie = "YF-V5-G0=69afb7c26160eb8b724e8855d7b705c6; _s_tentry=-; Apache=4397365853656.0835.1484723950063; SINAGLOBAL=4397365853656.0835.1484723950063; ULV=1484723950066:1:1:1:4397365853656.0835.1484723950063:; YF-Page-G0=046bedba5b296357210631460a5bf1d2; SUB=_2AkMvI80sf8NhqwJRmP0RxGPka4V1ww7EieKZfzz3JRMxHRl-yT83qkkptRBxtDWvOJvqp2I1Y85XsOvjpRF2kw..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WhXP5zlV.UkuM3K5Slq3ADu; YF-Ugrow-G0=9642b0b34b4c0d569ed7a372f8823a8e; WBtopGlobal_register_version=c689c52160d0ea3b";
//        String url = "http://s.weibo.com/weibo/" + URLEncoder.encode("王宝强", "UTF-8") + "&nodup=1";//"http://www.atool.org/useragent.php"
        String url = "http://weibo.com/p/1004061195242865/info?mod=pedit_more";
//		微博
//		String url="http://weibo.com/1026427652/E6QSaFvKQ?from=page_1005051026427652_profile&wvr=6&mod=weibotime&type=comment";
        System.out.println(url);
        CloseableHttpClient aDefault = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        String userAgent = "Mozilla/5.0 (Linux; Android 4.4.2; virtual machine Build/JWR66N) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 2.0.2 CK 2.0";
        httpGet.setHeader("User-Agent", userAgent);
        httpGet.setHeader("Cookie", cookie);
        HttpEntity entity = null;
        try {
            CloseableHttpResponse execute = aDefault.execute(httpGet);
            entity = execute.getEntity();
            String html = IOUtils.toString(entity.getContent(), "UTF-8");
            System.out.println(html);
            Document document = Jsoup.parse(html);
            Elements script = document.select("script");
            System.out.println(script.size() + " " + userAgent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            EntityUtils.consume(entity);
        }
        Thread.sleep(3000);
    }

    private Document getScriptHtml(Elements elements, String scriptId) {
        Document document = null;
        scriptId = "\"domid\":\"" + scriptId;
        for (Element element : elements) {
            String script = element.toString();
            if (script.contains(scriptId)) {
                String jsonStr = praseRegexSimple(script, "<script>FM.view\\((.*)\\)");
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                String html = jsonObject.getString("html");
                if (StringUtils.isNotBlank(html)) {
                    document = Jsoup.parse(html);
                    break;
                }
            }
        }
        return document;
    }

    public static String praseRegexSimple(String str,String regex){
        if(StringUtils.isNotEmpty(str)){
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            if(matcher.find()){
                return matcher.group(1);
            }
        }
        return null;
    }
}
