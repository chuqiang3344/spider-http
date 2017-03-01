package com.tyaer.net.http;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by Twin on 2016/8/24.
 */
public class HttpHandle {
    private static final String[] CHARSETS = {"UTF-8", "GBK", "GB2312", "GB18030", "BIG5"};
    protected static Logger logger = Logger.getLogger(HttpHandle.class);
    private static String[] ERROR_ENCODING = new String[]{"NULL", "ZH-CN", "ISO-88591", "ISO-8859-1"};

    /**
     * 方法名：getCharsetFromMetaTag
     * 描述：从meta标签中获取编码格式
     *
     * @param buffer
     * @return
     */
    public static String getCharsetFromMetaTag(ByteArrayBuffer buffer) {
        String charset = null;
        String html = new String(buffer.toByteArray());
        Document document = Jsoup.parse(html);
        Elements elements = document.select("meta");
        for (Element metaElement : elements) {
            if (metaElement.toString().contains("charset=")) {
                String meta_charset = metaElement.attr("charset");
                if (StringUtils.isNotBlank(meta_charset)) {
                    charset = meta_charset;
                    break;
                }
                String http_equiv = metaElement.attr("http-equiv");
                if (StringUtils.isNotBlank(http_equiv) && http_equiv.toLowerCase().equals("content-type")) {
                    String content = metaElement.attr("content");
                    charset = getCharSet(content);
                    if (StringUtils.isNotBlank(charset)) {
                        break;
                    }
                }
            }
        }
//		System.out.println("html charset："+charset);
//        if (StringUtils.isNotBlank(charset)) {
//            charset = charset.toUpperCase();
//            for (String s : CHARSETS) {
//                if (charset.contains(s)) {
//                    charset = s;
//                    break;
//                }
//            }
//        }
        return charset;
    }

    /**
     * 正则获取字符编码
     *
     * @param content
     * @return
     */
    public static String getCharSet(String content) {
        String regex = ".*charset=([^;]*).*";
//        String regex=".*?charset=(\\w+)[\\W]*?>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
            return matcher.group(1);
        else
            return null;
    }

    // 得到网站的返回编号
    public static int getStatusCode(HttpResponse httpResponse) {
        if (httpResponse != null) {
            int StatusCode = httpResponse.getStatusLine().getStatusCode();
            return StatusCode;
        } else {
            return 0;
        }
    }

    public static CookieStore getCookieStore(String str_cookies) {
        CookieStore cookieStore = new BasicCookieStore();
        if (StringUtils.isNotBlank(str_cookies)) {
            String[] split = str_cookies.split(";");
            for (String str_cookie : split) {
                String[] cookie_kv = str_cookie.split("=");
//            System.out.println(cookie_kv.length);
                BasicClientCookie basicClientCookie = new BasicClientCookie(cookie_kv[0], cookie_kv[1]);
                cookieStore.addCookie(basicClientCookie);
            }
        }
        return cookieStore;
    }

    public static void main(String[] args) throws IOException {
//        String sinaCookieInit = "SINAGLOBAL=4505639076232.91.1435475046858; SUB=_2AkMi-9s7f8NhqwJRmPoXxGzlaI51zwrEiebDAHzsJxJjHi087T8Cs_4NR4zkC7tCjBiXTYEdzAmP8Q..; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9WFREgYKEBjL_nL_00-n.4wJ; YF-Ugrow-G0=ad06784f6deda07eea88e095402e4243; _s_tentry=-; Apache=9866070658899.844.1438135556807; ULV=1438135556815:8:5:4:9866070658899.844.1438135556807:1438134856361;YF-V5-G0=2a21d421b35f7075ad5265885eabb1e4; WBStore=4e40f953589b7b00|undefined; UOR=finance.sina.com.cn,weibo.com,bbs.flyme.cn;";
//        System.out.println(getCookieStore(sinaCookieInit));
//        String xx = FileUtils.readFileToString(new File("./file/test.html"), "utf-8");
        String xx = "text/html; charset=utf-8";
        System.out.println(xx);
        System.out.println(getCharSet(xx));
    }

    public static String getCharset(HttpEntity entity, ByteArrayBuffer byteArrayBuffer) {
        String encoding = null;
        /**
         * 仿浏览器获取网页编码
         * 浏览器是先从content-type的charset（响应头信息）中获取编码，
         * 如果获取不了，则会从meta（HTML里的代码）中获取charset的编码值
         */
        //第一步：处理网页字符编码
        ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();
        if (null != charset) {
            encoding = charset.toString();
        }
        //第二步--->如果第一步contenttyp未获取到编码，这里从meta标签中获取
        if (StringUtils.isBlank(encoding) || ArrayUtils.contains(ERROR_ENCODING, encoding.toUpperCase())) {
            encoding = getCharsetFromMetaTag(byteArrayBuffer);
        }
        if (StringUtils.isBlank(encoding)) {
            encoding = "GB2312";
        }
        return encoding.trim();
    }

    public static String downLoadImg(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        String result = null;
        try {
            CloseableHttpResponse httpResponse = client.execute(httpGet);
            InputStream inputStream = httpResponse.getEntity().getContent();
            String picName = getCurrentTime("yyyyMMdd-hhmmss");
            File file = new File("./file/" + picName + ".png");
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();

            outputStream.close();
            inputStream.close();
            result = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpGet.releaseConnection();
        }
        return result;
    }

    public static String getCurrentTime(String format) {

        Calendar calendar = Calendar.getInstance();
        //format=yyyyMMdd-hhmmss//
        SimpleDateFormat formats = new SimpleDateFormat(format);
        String time = formats.format(calendar.getTime());
        //System.out.println(time);
        return time;
    }

    public static Integer getInteger(String str) {
        Integer integer = 0;
        if (StringUtils.isNotBlank(str)) {
            str = str.trim();
            try {
                integer = Integer.valueOf(str);
            } catch (Exception e) {
                System.out.println("string getInteger fail:" + str);
            }
        }
        return integer;
    }

    public String getCharset(HttpEntity entity) {
        String encoding = "GBK";
        /**
         * 仿浏览器获取网页编码
         * 浏览器是先从content-type的charset（响应头信息）中获取编码，
         */
        //第一步：处理网页字符编码
        ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();
        if (null != charset) {
            encoding = charset.toString();
        }
        return encoding;
    }

    public ByteArrayBuffer readToByteBuffer(HttpEntity entity) throws IOException {
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(4096);
        //判断返回的数据流是否采用了gzip压缩
        Header header = entity.getContentEncoding();
        boolean isGzip = false;
        if (null != header) {
            for (HeaderElement headerElement : header.getElements()) {
                if (headerElement.getName().equalsIgnoreCase("gzip")) {
                    isGzip = true;
                }
            }
        }
        //获得响应流
        InputStream inputStream = entity.getContent();
        GZIPInputStream gzipInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            byte[] tmp = new byte[4096];
            int count;
            if (isGzip) {//如果采用了Gzip压缩，则进行gizp压缩处理
                gzipInputStream = new GZIPInputStream(inputStream);
                while ((count = gzipInputStream.read(tmp)) != -1) {
                    byteArrayBuffer.append(tmp, 0, count);
                }
            } else {//处理非gzip格式的数据
                bufferedInputStream = new BufferedInputStream(inputStream);
                while ((count = bufferedInputStream.read(tmp)) != -1) {
                    byteArrayBuffer.append(tmp, 0, count); // TODO: bufferedInputStream.read(tmp) 内存溢出
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (gzipInputStream != null) {
                gzipInputStream.close();
            }
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
        }
        return byteArrayBuffer;
    }

    /**
     *
     * @param buffer
     * @param url
     * @return
     */
    public String getCharsetFromMetaTag(ByteArrayBuffer buffer, String url) {
        String charset = null;
        String regEx = "charset=\"(.+?)\"";
        Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(new String(buffer.toByteArray()));
        boolean result = m.find();
        if (result) {
            if (m.groupCount() == 1) {
                charset = m.group(1);
            }
            System.err.println("网页 中的编码:" + charset + "\t url:" + url);
        } else {
            //出现未匹配的编码，先赋值为gbk
            charset = "gbk";
            System.out.println("字符编码未匹配到 : " + url);
        }
        return charset;
    }

    /*
    * 方法名：replaceStr
    * 作者：zhouyh
    * 创建时间：2015-10-14 下午05:33:01
    * 描述：替换原网页中的特殊字符
    * @param src
    * @return
    */
    public String replaceStr(String src) {
        if (src == null || "".equals(src)) return null;
        src = src.replaceAll("<!--", "");
        src = src.replaceAll("-->", "");
        src = src.replaceAll("<", "<");
        src = src.replaceAll(">", ">");
        src = src.replaceAll("\"", "\"");
        src = src.replaceAll(" ", " ");
        src = src.replaceAll("&", "&");
        return src;
    }
}
