package com.tyaer.net.app;

import com.tyaer.net.bean.ResponseBean;
import com.tyaer.net.http.HttpHelper;

/**
 * Created by Twin on 2017/3/1.
 */
public class HttpHelper_Test {

    public static void main(String[] args) {
//        ResponseBean responseBean = HttpHelper.sendRequest("http://weibo.com/p/1005051931655382/follow?relate=fans&from=100505&wvr=6&mod=headfans&current=fans#place","SINAGLOBAL=1316671036183.834.1486689445310; wb_g_upvideo_6108180470=1; wb_publish_fist100_6108180470=1; wvr=6; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; SSOLoginState=1488328781; _s_tentry=login.sina.com.cn; UOR=,,login.sina.com.cn; Apache=5801257388666.272.1488328783792; YF-V5-G0=3737d4e74bd7e1b846a326489cdaf5ab; ULV=1488328783902:24:1:5:5801257388666.272.1488328783792:1488274552042; YF-Ugrow-G0=56862bac2f6bf97368b95873bc687eef; WBtopGlobal_register_version=ad0d6baf029c6e6e; SCF=ApZe9mqcNg15KUTXMMH6QQfcobOg9XbVVRNxjnB0KFclDlCWm_cjUr13d0VRDVambTWH-Ts-OSA43T9wgUcXKc0.; SUB=_2A251svXWDeRxGeBP61oQ-C7IzDyIHXVWxmAerDV8PUJbmtBeLUjdkW82aA_UGe07ZEVSMmX_1Kyu7b2rqg..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5ykf6kg2mCLg_E_Ep99F5j5JpX5o2p5NHD95QceK5ReKn7ShM7Ws4Dqcj_i--ci-zfiKnpi--NiKnpi-8Fi--Xi-zRi-zci--Xi-z4iKyFi--ci-82iKyh; SUHB=0HTR7gYT2bY3Qw; ALF=1519810545");
//        ResponseBean responseBean = HttpHelper.sendRequest("http://m.weibo.cn/u/2640113513","_T_WM=253a47b6813cf51100a406158744e690; M_WEIBOCN_PARAMS=featurecode%3D20000180%26oid%3D4077215773654155%26luicode%3D10000011%26lfid%3D1005052640113513%26fid%3D1076032640113513%26uicode%3D10000011");
        ResponseBean responseBean = HttpHelper.sendRequest("http://m.weibo.cn/container/getIndex?type=uid&value=2640113513&containerid=1076032640113513");
        String rawText = responseBean.getRawText();
        System.out.println(rawText);
    }
}
