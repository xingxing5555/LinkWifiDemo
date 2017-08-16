package com.zlw.main.linkwifidemo;

import android.net.wifi.ScanResult;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.common.collect.Lists;

public class WifiConnectAdapter extends BaseQuickAdapter<ScanResult, BaseViewHolder> {

    public WifiConnectAdapter() {
        super(R.layout.rv_item_wifi_list, Lists.<ScanResult>newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, ScanResult item) {
        viewHolder
                .setText(R.id.etWifiSSID, item.SSID)
                .setText(R.id.wifiLevel, "信号: " + item.level)
                .setText(R.id.wifiContext, item.toString());
    }

}