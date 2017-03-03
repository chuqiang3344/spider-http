package us.codecraft.webmagic.downloader;

import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.selector.Html;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;

/**
 * @author code4crafer@gmail.com
 */
public class HttpClientDownloaderTest {

    public static final String PAGE_ALWAYS_NOT_EXISTS = "http://localhost:13421/404";

    @Test
    public void testDownloader() {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//        Html html = httpClientDownloader.download("https://www.baidu.com/");
        Html html = httpClientDownloader.download("http://www.jb51.net/article/73690.htm");
        System.out.println(html);
        assertTrue(!html.getFirstSourceText().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDownloaderInIllegalUrl() throws UnsupportedEncodingException {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.download("http://www.oschina.net/>");
    }

    @Test
    public void testCycleTriedTimes() {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        Task task = Site.me().setDomain("localhost").setCycleRetryTimes(5).toTask();
        Request request = new Request(PAGE_ALWAYS_NOT_EXISTS);
        Page page = httpClientDownloader.download(request, task);
//        assertThat(page.getTargetRequests().size() > 0);
//        assertThat((Integer) page.getTargetRequests().get(0).getExtra(Request.CYCLE_TRIED_TIMES)).isEqualTo(1);
        page = httpClientDownloader.download(page.getTargetRequests().get(0), task);
//        assertThat((Integer) page.getTargetRequests().get(0).getExtra(Request.CYCLE_TRIED_TIMES)).isEqualTo(2);
    }


}
