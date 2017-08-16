package com.zlw.main.linkwifidemo;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zlw.main.linkwifidemo.utils.Logger;
import com.zlw.main.linkwifidemo.utils.blankj.PhoneUtils;

import cn.jpush.android.api.JPushInterface;

public class MyApp extends Application {
    private static final String TAG = MyApp.class.getSimpleName();
    private static MyApp ins;

    public static MyApp getInstance() {
        return ins;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "=========>>MyApp<<========= ");
        ins = this;
        init();
    }

    void init() {
        //JPush
        Logger.d(TAG, "初始化 JPush 中... ");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        String imei = PhoneUtils.getIMEI();
        Logger.i(TAG, "本机imei、Jpush 别名:  " + imei);
        JPushInterface.setAlias(getApplicationContext(), 1, "" + imei);

        //ZXing
        ZXingLibrary.initDisplayOpinion(this);
    }
}
