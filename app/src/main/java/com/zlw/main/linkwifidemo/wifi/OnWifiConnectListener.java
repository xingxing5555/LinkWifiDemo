package com.zlw.main.linkwifidemo.wifi;


public interface OnWifiConnectListener {

    void onStart(String SSID);

    void onFailure(String SSID);

    void onFinish();
}
