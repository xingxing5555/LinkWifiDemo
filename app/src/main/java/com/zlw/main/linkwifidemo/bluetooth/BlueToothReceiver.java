package com.zlw.main.linkwifidemo.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BlueToothReceiver extends BroadcastReceiver {

    private static final String TAG = BlueToothReceiver.class.getSimpleName();
    private final BlueToothActionListener mListener;

    public BlueToothReceiver(BlueToothActionListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == mListener) {
            return;
        }
        String action = intent.getAction();
        if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_NONE:
                    mListener.onCancle();
                    break;
                case BluetoothDevice.BOND_BONDING:
                    mListener.onBinding();
                    break;
                case BluetoothDevice.BOND_BONDED:
                    mListener.onSuccess();
                    break;
            }
        }
    }
}
