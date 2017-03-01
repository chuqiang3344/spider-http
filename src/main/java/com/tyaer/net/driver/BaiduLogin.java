package com.tyaer.net.driver;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Set;

/**
 * Created by Twin on 2016/11/10.
 */

public class BaiduLogin {

    private static Logger LOGGER = Logger.getLogger(BaiduLogin.class);

    public static void main(String[] args) throws Exception {
        String cookie = getBaiduCookie("96a3764", "fcfxio96057");
        System.out.println(cookie);
    }


    public static String getBaiduCookie(String username, String password) throws Exception {

        HtmlUnitDriver driver = new HtmlUnitDriver(true);
        // driver.setSocksProxy("127.0.0.1", 1080);
        // driver.setProxy("127.0.0.1", 1080);
        // HtmlOption htmlOption=new Ht
        // WebDriver driver = new FirefoxDriver();
        driver.setJavascriptEnabled(true);
        // user agent switcher//
        String loginAddress = "https://passport.baidu.com/v2/?login&tpl=mn&u=http%3A%2F%2Fwww.baidu.com%2F";
        driver.get(loginAddress);

        WebElement element_username = driver.findElementByCssSelector("input[id=TANGRAM__PSP_3__userName]");
        element_username.sendKeys(username);
//		WebElement element_password = driver.findElementByCssSelector("input[id=TANGRAM__PSP_3__password]");
        WebElement element_password = driver.findElement(By.id("TANGRAM__PSP_3__password"));
        element_password.click();
        element_password.sendKeys(password);

        WebElement ele = driver.findElementByCssSelector("img[id=TANGRAM__PSP_3__verifyCodeImg]");
        String src = ele.getAttribute("src");
        String cookie = concatCookie(driver);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(1000)
                .setConnectTimeout(1000)
                .setCookieSpec(cookie)
                .build();

        HttpGet httpget = new HttpGet(src);
        httpget.setConfig(requestConfig);
        CloseableHttpResponse response = httpclient.execute(httpget);

        HttpEntity entity;
        String result = null;
        try {

            if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
                // try again//
            }
            entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    // do something useful
                    InputStream inputStream = entity.getContent();
                    BufferedImage img = ImageIO.read(inputStream);
                    // deal with the weibo captcha //
                    String picName = LoginUtils.getCurrentTime("yyyyMMdd-hhmmss");
                    // 新建captcha目录//
                    LoginUtils.getCaptchaDir();
                    picName = "./captcha/baidu-" + picName + ".png";
                    ImageIO.write(img, "png", new File(picName));
                    String userInput = new CaptchaFrame(img).getUserInput();
                    WebElement code = driver.findElementByCssSelector("input[id=TANGRAM__PSP_3__verifyCode]");
                    code.sendKeys(userInput);
//					WebElement rem = driver.findElementByCssSelector("input[name=remember]");
//					rem.click();
                    WebElement submit = driver.findElementByCssSelector("input[id=TANGRAM__PSP_3__submit]");
                    // 错误捕获//
                    submit.click();
                    result = concatCookie(driver);
                    driver.close();
                } finally {
                    instream.close();
                }
            }
        } finally {
            response.close();
        }
        driver.quit();
        return result;
    }

    public static String retryGetSinaCookie(String username, String password) {
        String cookie = "";
        //
        try {
            do {
                cookie = "";
                cookie = getBaiduCookie(username, password);
                // 没有得到正确的cookie则间隔10秒钟继续响应服务器//
                if (!StringUtils.isEmpty(cookie)) {
                    break;
                } else {
                    LOGGER.error("response server failed,will sleep 10 seconds,then try again...");
//					LoginUtils.sleep(10 * 1000);
                }
            } while (true);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cookie;

    }

    public static String concatCookie(HtmlUnitDriver driver) {
        Set<Cookie> cookieSet = driver.manage().getCookies();
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : cookieSet) {
            sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
        }
        String result = sb.toString();
        return result;
    }

    public static class CaptchaFrame {

        JFrame frame;
        JPanel panel;
        JTextField input;
        int inputWidth = 100;
        BufferedImage img;
        String userInput = null;

        public CaptchaFrame(BufferedImage img) {
            this.img = img;
        }

        public String getUserInput() {
            frame = new JFrame("Captcha Input");
            final int imgWidth = img.getWidth();
            final int imgHeight = img.getHeight();
            int width = imgWidth * 2 + inputWidth * 2;
            int height = imgHeight * 4;
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int startx = (dim.width - width) / 2;
            int starty = (dim.height - height) / 2;
            frame.setBounds(startx, starty, width, height);
            frame.setTitle("验证码输入框");
            // String iconPath = "./icon/weibo-4.png";
            String iconPath = "./icon/weibo-1.jpg";
            ImageIcon testImg = new ImageIcon(new String(iconPath));
            frame.setIconImage(testImg.getImage());
            // 得到一个Toolkit对象
            // Toolkit tool = frame.getToolkit();
            // 由tool获取图像
            // Image myimage = tool.getImage(iconPath);
            // frame.setIconImage(myimage);
            Container container = frame.getContentPane();
            container.setLayout(new BorderLayout());
            panel = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(img, 0, 0, imgWidth * 2, imgHeight * 2, null);
                }
            };
            panel.setLayout(null);
            container.add(panel);
            input = new JTextField(6);
            input.setBounds(imgWidth * 2, 0, inputWidth, imgHeight * 2);
            panel.add(input);
            JButton btn = new JButton("确定");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    userInput = input.getText().trim();
                    synchronized (CaptchaFrame.this) {
                        CaptchaFrame.this.notify();
                    }
                }
            });
            input.addKeyListener(new KeyAdapter() {
                // 输入内容若为回车键则结束，捕获该事件//
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        userInput = input.getText().trim();
                        synchronized (CaptchaFrame.this) {
                            CaptchaFrame.this.notify();
                        }
                    }
                }
            });

            // ============================
            btn.setBounds(imgWidth * 2 + inputWidth, 0, inputWidth, imgHeight * 2);
            panel.add(btn);
            frame.setVisible(true);
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            frame.dispose();
            return userInput;
        }
    }
}
