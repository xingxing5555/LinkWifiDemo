package com.zlw.main.linkwifidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zlw.main.linkwifidemo.bluetooth.BlueToothActivity;
import com.zlw.main.linkwifidemo.wifi.WifiActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btBlueTooth, R.id.btWifi, R.id.btHotspot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btBlueTooth:
                BlueToothActivity.startMe(this);
                break;
            case R.id.btWifi:
                WifiActivity.startMe(this);
                break;
            case R.id.btHotspot:

                break;
        }
    }
}
