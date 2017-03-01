package com.tyaer.net.driver;

import org.apache.log4j.Logger;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Created by Twin on 2016/11/10.
 */

public class Driver_Test {

    private static Logger LOGGER = Logger.getLogger(Driver_Test.class);

    public static void main(String[] args) throws Exception {

        HtmlUnitDriver driver = new HtmlUnitDriver(true);
        // driver.setSocksProxy("127.0.0.1", 1080);
        // driver.setProxy("127.0.0.1", 1080);
        // HtmlOption htmlOption=new Ht
        // WebDriver driver = new FirefoxDriver();
        driver.setJavascriptEnabled(true);
        // user agent switcher//
        String loginAddress = "http://data.stats.gov.cn/easyquery.htm?cn=E0104";
        driver.get(loginAddress);
        System.out.println(driver.getPageSource());

        driver.quit();
    }

}
