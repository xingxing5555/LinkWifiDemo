package com.zlw.main.linkwifidemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.collect.Lists;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zlw.main.linkwifidemo.utils.JsonUtil;
import com.zlw.main.linkwifidemo.utils.Logger;
import com.zlw.main.linkwifidemo.utils.blankj.PhoneUtils;
import com.zlw.main.linkwifidemo.wifi.WifiConnectBean;
import com.zlw.main.linkwifidemo.wifi.WifiController;
import com.zlw.main.linkwifidemo.wifi.WifiReceiver;
import com.zlw.main.linkwifidemo.wifi.WifiReceiverActionListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 0011;
    @BindView(R.id.btOpenWifi)
    Button btOpenWifi;
    @BindView(R.id.btSelectWifi)
    Button btSelectWifi;
    @BindView(R.id.btCreateQrcode)
    Button btCreateQrcode;
    @BindView(R.id.btScan)
    Button btScan;
    @BindView(R.id.rvWifiList)
    RecyclerView rvWifiList;
    @BindView(R.id.ivQRcode)
    ImageView ivQRcode;

    private Unbinder bind;

    private WifiController wifiController;
    private WifiReceiver wifiReceiver;

    private WifiConnectAdapter adapter;
    private List<ScanResult> wifiList;

    private AlertDialog.Builder wifiLinkDialogBuilder;
    private WifiLinkDialogViewHolder wifiLinkDialogViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(TAG, "=====================>>Mainactivity<<=====================");
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        init();
        initRecycleView();
    }

    @Override
    protected void onDestroy() {
        //注销广播
        if (wifiReceiver != null) {
            unregisterReceiver(wifiReceiver);
            Logger.d(TAG, "-----注销广播接收器：WifiReceiver-----");
        }

        bind.unbind();
        Logger.d(TAG, "=====<<Mainactivity>>-onDestroy=====");
        super.onDestroy();
    }

    @OnClick({R.id.btOpenWifi, R.id.btSelectWifi, R.id.btCreateQrcode, R.id.btScan, R.id.btAll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btOpenWifi:
                openWifi();
                break;
            case R.id.btSelectWifi:
                selectWifi();
                break;
            case R.id.btCreateQrcode:
                createQRcode();
                break;
            case R.id.btScan:
                startScan();
                break;
            case R.id.btAll:
                startAll();
                break;
        }
    }

    private void initRecycleView() {
        rvWifiList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WifiConnectAdapter();
        rvWifiList.setAdapter(adapter);
        wifiList = Lists.newArrayList();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ScanResult item = wifiList.get(position);
                showWifiLinkDialog(item);
            }
        });

        View header = getLayoutInflater().inflate(R.layout.rv_header_wifi_list, (ViewGroup) rvWifiList.getParent(), false);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWifiLinkDialog(null);
            }
        });
        adapter.setHeaderView(header);
    }

    private void initWifiLinkDialog(View dialog) {
        if (dialog == null) {
            throw new NullPointerException("Dialog is null.");
        }

        wifiLinkDialogBuilder = new AlertDialog.Builder(this);
        wifiLinkDialogBuilder.setTitle("请输入Wifi信息");
        wifiLinkDialogBuilder.setPositiveButton("远程连接", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (wifiLinkDialogViewHolder != null) {
                    WifiConnectBean wifiLinkInfo = wifiLinkDialogViewHolder.getWifiLinkInfo();
                    String jsonStr = JsonUtil.toJson(wifiLinkInfo);
                    wifiLinkDialogViewHolder = null;
                    wifiLinkDialogBuilder = null;
                    WifiConnectActivity.startMe(MainActivity.this, jsonStr);

                } else {
                    Logger.e(TAG, "wifiLinkDialogViewHolder is NULL ");
                }
            }
        });
        wifiLinkDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                wifiLinkDialogViewHolder = null;
                wifiLinkDialogBuilder = null;

            }
        });
        wifiLinkDialogBuilder.setView(dialog);
        wifiLinkDialogBuilder.setCancelable(false);
    }

    private void showWifiLinkDialog(ScanResult item) {
        Logger.d(TAG, "---show WifiLinkDialog---");
        View dialogView;
        if (wifiLinkDialogViewHolder == null) {
            LayoutInflater inflater = getLayoutInflater();
            dialogView = inflater.inflate(R.layout.dialog_wifi_link, null);
        } else {
            dialogView = wifiLinkDialogViewHolder.dialogView;
        }

        wifiLinkDialogViewHolder = new WifiLinkDialogViewHolder(dialogView);
        if (null != item) {
            WifiController.SecurityMode securityMode = WifiController.getInstant(getApplicationContext()).getSecurityMode(item);
            wifiLinkDialogViewHolder.etWifiSSID.setText(item.SSID);
            wifiLinkDialogViewHolder.tvWifiSecurityMode.setText(securityMode.name());
        }

        if (wifiLinkDialogBuilder == null) {
            initWifiLinkDialog(dialogView);
        }
        wifiLinkDialogBuilder.setCancelable(false);
        wifiLinkDialogBuilder.show();
    }


    private WifiReceiverActionListener wifiReceiverActionListener = new WifiReceiverActionListener() {
        @Override
        public void onWifiOpened() {
            Logger.d(TAG, "-----WifiReceiverActionListener.onWifiOpened():-----");
        }

        @Override
        public void onWifiOpening() {
            Logger.d(TAG, "-----WifiReceiverActionListener.onWifiOpening():-----");
        }

        @Override
        public void onWifiClosed() {
            Logger.d(TAG, "-----WifiReceiverActionListener.onWifiClosed():-----");
        }

        @Override
        public void onWifiClosing() {
            Logger.d(TAG, "-----WifiReceiverActionListener.onWifiClosing():-----");
        }

        @Override
        public void onWifiScanResultBack() {
            Logger.d(TAG, "-----WifiReceiverActionListener.onWifiScanResultBack():-----");
            List<ScanResult> wifiScanResultList = wifiController.getWifiScanResult();
            Logger.i(TAG, "-----connectionInfo:-----");
            Logger.i(TAG, "size: " + wifiScanResultList.size());
            Logger.i(TAG, wifiScanResultList.get(0).toString() + "");
            wifiList = wifiScanResultList;
            adapter.setNewData(wifiList);
            Logger.i(TAG, "------------------------------");
        }

        @Override
        public void onWifiConnected(WifiInfo wifiInfo) {
            Logger.d(TAG, "-----WifiReceiverActionListener.onWifiConnected():-----");
        }
    };

    private void init() {
        MainActivityPermissionsDispatcher.selectWifiWithCheck(this);
        MainActivityPermissionsDispatcher.startScanWithCheck(this);
        Logger.d(TAG, "初始化 wifiController...");
        wifiController = WifiController.getInstant(getApplicationContext());
        //动态注册广播接收器
        Logger.d(TAG, "-----注册广播接收器：WifiReceiver-----");
        wifiReceiver = new WifiReceiver(wifiReceiverActionListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        registerReceiver(wifiReceiver, intentFilter);
    }


    public void openWifi() {
        Logger.d(TAG, "enableWifi...");
        wifiController.enableWifi();
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void selectWifi() {
        ivQRcode.setVisibility(View.GONE);

        if (wifiController == null) {
            Logger.w(TAG, "wifiController == null");
            return;
        }
        Logger.d(TAG, "isLocationEnabled.：" + isLocationEnabled());
        if (!isLocationEnabled()) {
            Toast.makeText(this, "请开启定位权限", Toast.LENGTH_SHORT).show();
            return;
        }

        Logger.d(TAG, "开始扫描Wifi...");
        wifiController.scanWifiAround();
    }

    private boolean isLocationEnabled() {
        return Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF) != Settings.Secure.LOCATION_MODE_OFF;
    }

    class WifiLinkDialogViewHolder {
        View dialogView;

        @BindView(R.id.etWifiSSID)
        EditText etWifiSSID;
        @BindView(R.id.tvWifiSecurityMode)
        TextView tvWifiSecurityMode;
        @BindView(R.id.etWifiPassword)
        EditText etWifiPassword;

        WifiLinkDialogViewHolder(View view) {
            ButterKnife.bind(this, view);
            this.dialogView = view;
        }

        WifiConnectBean getWifiLinkInfo() {
            if (TextUtils.isEmpty(etWifiPassword.getText().toString())) {
                Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                return null;
            }
            if (TextUtils.isEmpty(etWifiSSID.getText().toString())) {
                Logger.e(TAG, "Wifi SSID 异常");
                return null;
            }

            if (TextUtils.isEmpty(tvWifiSecurityMode.getText().toString())) {
                Logger.e(TAG, "Wifi SecurityMode 数据异常");
                return null;
            }
            return new WifiConnectBean(etWifiSSID.getText().toString(), etWifiPassword.getText().toString(), tvWifiSecurityMode.getText().toString());
        }
    }


    //二维码相关
    @NeedsPermission(Manifest.permission.CAMERA)
    public void startScan() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void createQRcode() {
        String textContent = PhoneUtils.getIMEI();
        Bitmap mBitmap = CodeUtils.createImage(textContent, 400, 400, null);

        ivQRcode.setImageBitmap(mBitmap);
        ivQRcode.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "绑定成功，请选择Wifi", Toast.LENGTH_LONG).show();
                    Logger.i(TAG, "解析结果:" + result);
                    selectWifi();

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    Logger.i(TAG, "解析二维码失败");
                }
            }
        }
    }

    /**
     * Wifi连接完整流程
     * 1.扫描设备二维码获取设备IMEI码
     * 2.录入Wifi信息  WiFiContentBean
     * 3.将IMEI码和WiFiContentBean 发送给服务器，服务器再推送给目标设备
     * 4.设备获取Wifi信息 并自动连接
     */
    private void startAll() {
        startScan();
    }
}
