package com.zlw.main.linkwifidemo.wifi;

/**
 * Created by admin on 2017/8/11.
 */

public interface OnWifiConnectListener {

    void onStart(String SSID);

    void onFailure(String SSID);

    void onFinish();
}
