package com.zlw.main.linkwifidemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.zlw.main.linkwifidemo.utils.JsonUtil;
import com.zlw.main.linkwifidemo.utils.Logger;
import com.zlw.main.linkwifidemo.wifi.OnWifiConnectListener;
import com.zlw.main.linkwifidemo.wifi.WifiConnectBean;
import com.zlw.main.linkwifidemo.wifi.WifiController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiConnectActivity extends AppCompatActivity {
    private static final String TAG = WifiConnectActivity.class.getSimpleName();
    public static final String BEAN = "BEAN";
    @BindView(R.id.tvWifiLinkInfo)
    TextView tvWifiLinkInfo;
    @BindView(R.id.btStartLink)
    Button btStartLink;

    private WifiConnectBean wifiConnectBean;

    /**
     * @param context
     * @param jsonStr WifiConnectBean-json
     */
    public static void startMe(Context context, String jsonStr) {
        Logger.e(TAG, "WifiConnectActivity startMe");

        if (TextUtils.isEmpty(jsonStr)) {
            Logger.e(TAG, "WifiConnectActivity.startMe()  jsonStr =  NULL");
            return;
        }

        Intent intent = new Intent(context, WifiConnectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BEAN, jsonStr);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wif_link);
        ButterKnife.bind(this);
        Logger.d(TAG, "=====================>>WifiConnectActivity<<=====================");
        initData();
    }

    private void initData() {
        String jsonStr = getIntent().getStringExtra(BEAN);
        if (TextUtils.isEmpty(jsonStr)) {
            Logger.e(TAG, "initData failed");
            finish();
            return;
        } else {
            wifiConnectBean = JsonUtil.fromJson(jsonStr, WifiConnectBean.class);
        }
        tvWifiLinkInfo.setText(jsonStr);
        Logger.i(TAG, wifiConnectBean.toString());
    }


    @Override
    protected void onDestroy() {
        Logger.d(TAG, "=====<<WifiConnectActivity>>-onDestroy=====");
        super.onDestroy();
    }

    @OnClick(R.id.btStartLink)
    public void onViewClicked() {
        Logger.d(TAG, "开始连接Wifi...");
        WifiController.getInstant(getApplicationContext()).connectWifiByPassword(wifiConnectBean.getWifiSSID(),
                WifiController.SecurityMode.valueOf(wifiConnectBean.getWifiSecurityMode()),
                wifiConnectBean.getWifiPassword(), new OnWifiConnectListener() {
                    @Override
                    public void onStart(String SSID) {
                        btStartLink.setText("正在连接");
                        Logger.d(TAG, "正在连接...");
                    }

                    @Override
                    public void onFailure(String SSID) {
                        btStartLink.setText("连接失败,点击重试");
                        Logger.d(TAG, "连接失败");
                    }

                    @Override
                    public void onFinish() {
                        btStartLink.setText("连接成功");
                        Logger.d(TAG, "连接成功");
                    }
                });
    }
}
