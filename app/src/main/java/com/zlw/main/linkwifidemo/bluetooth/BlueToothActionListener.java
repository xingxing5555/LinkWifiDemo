package com.zlw.main.linkwifidemo.bluetooth;

/**
 * Created by admin on 2017/8/30.
 */

public interface BlueToothActionListener {

    //取消配对
    void onCancle();

    //配对中
    void onBinding();

    //配对成功
    void onSuccess();
}
