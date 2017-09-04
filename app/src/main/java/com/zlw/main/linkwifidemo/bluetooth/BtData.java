package com.zlw.main.linkwifidemo.bluetooth;

/**
 * Created by zhaolewei on 2017/9/1.
 */
public class BtData {
    private String imei;
    private String imsi;
    private String sn;

    private String type; // 限定范围: wifi,mifi,msg，translate_language,
    private String username; //wifi或mifi中的UUID
    private String password; //wifi密码或Mifi密码
    private String securityMode; //加密类型: OPEN, WEP, WPA, WPA2  ,默认WPA
    private String msg;//错误描述、语言()
    private int errorCode; //200 成功


    //type 描述：1.Wifi连接参数。2.Mifi热点修复信息。 3.操作结果 4.选择翻译语言

    //示例：type="wifi"; username="easyto_dev";password="12345678";securityMode="WPA"
    /*
    {
        type:"wifi"

    }
    */

//    服务器：时间戳
}

