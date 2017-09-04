package com.zlw.main.linkwifidemo.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.common.collect.Lists;
import com.zlw.main.linkwifidemo.R;

public class BluetoothConnectAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {

    public BluetoothConnectAdapter() {
        super(R.layout.rv_item_bluetooth_list, Lists.<BluetoothDevice>newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, BluetoothDevice item) {
        /**
         * BondState状态说明：
         * BOND_NONE  10 ： 未进行配对
         * BOND_BONDING 11 ：配对中
         * BOND_BONDED 12 ：已配对
         * -
         * BluetoothClass蓝牙设备类型说明
         * http://m.blog.csdn.net/angcyo/article/details/52048762
         */
        String deviceName;
        if (item.getBluetoothClass() != null) {
            deviceName = BlueToothNameHelper.getDeviceName(Integer.parseInt(String.valueOf(item.getBluetoothClass().getMajorDeviceClass())));
        } else {
            deviceName = "Null";
        }
        viewHolder
                .setText(R.id.tvBluetoothName, item.getName())
                .setText(R.id.tvBluetoothAddress, item.getAddress())
                .setText(R.id.tvBluetoothContext, "状态：" + BlueToothNameHelper.getStatusName(item.getBondState()) + " ; 设备： "
                        + deviceName);
    }
}