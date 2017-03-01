package com.tyaer.net.manager;


import com.tyaer.net.bean.ResponseBean;
import com.tyaer.net.http.HttpHelper;
import org.junit.Test;

import java.util.Random;

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
     * 根据指定最小值与最大值生成随机数。最小值为0时，则最大值只有max-1
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
    public void zxc(){
        ResponseBean responseBean = HttpHelper.sendRequest("http://weixin.sogou.com/weixin?type=2&query=%E9%98%BF%E8%90%A8%E5%BE%B7&ie=utf8&_sug_=y&_sug_type_=&w=&sut=4780&sst0=1482373135364&lkt=5%2C1482373130391%2C1482373132365");
        System.out.println(responseBean.getRawText());
    }

}
