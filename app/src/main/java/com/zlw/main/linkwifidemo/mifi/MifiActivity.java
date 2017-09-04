package com.zlw.main.linkwifidemo.mifi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zlw.main.linkwifidemo.R;
import com.zlw.main.linkwifidemo.utils.Logger;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MifiActivity extends AppCompatActivity {

    private static final String TAG = MifiActivity.class.getSimpleName();
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    private WifiManager wifiManager;

    public static void startMe(Context context) {
        context.startActivity(new Intent(context, MifiActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MifiActivityPermissionsDispatcher.getPermissionWithCheck(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mifi);
        ButterKnife.bind(this);
        //获取wifi管理服务
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

    }

    @OnClick({R.id.btStart, R.id.btClose,R.id.btGetMsg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btStart:
                createMifi("zlw_test_mifi", "123456789");
                tvMsg.setText("当前MIFI: zlw_test_mifi, 123456789");
                break;
            case R.id.btClose:
                closeMifi();
                break;
            case R.id.btGetMsg:
                getConfig();
                break;
        }
    }


    /**
     * @param
     * @param
     * @return
     */
    @NeedsPermission({Manifest.permission.WRITE_SETTINGS})
    public void getPermission() {

    }

    /**
     * 需要权限：android.permission.WRITE_SETTINGS
     */
    public void createMifi(String ssid, String password) {
        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称(可以在名字后面加点随机数什么的)
            apConfig.SSID = ssid;
            //配置热点的密码
            apConfig.preSharedKey = password;
            //通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            //返回热点打开状态
            method.invoke(wifiManager, apConfig, true);
        } catch (Exception e) {
            Logger.e(e, TAG, e.getMessage());
        }
    }

    private boolean closeMifi() {
        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称(可以在名字后面加点随机数什么的)
            apConfig.SSID = "";
            //配置热点的密码
            apConfig.preSharedKey = "";
            //通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            //返回热点打开状态
            return (Boolean) method.invoke(wifiManager, apConfig, false);
        } catch (Exception e) {
            Logger.e(e, TAG, e.getMessage());
            return false;
        }
    }


    public void getConfig() {
        try {
            Method method = wifiManager.getClass().getMethod(
                    "getWifiApConfiguration");
            //返回热点打开状态
            WifiConfiguration configuration = (WifiConfiguration) method.invoke(wifiManager);
            tvMsg.setText(configuration.toString());
        } catch (Exception e) {
            Logger.e(e, TAG, e.getMessage());
        }
    }
}
