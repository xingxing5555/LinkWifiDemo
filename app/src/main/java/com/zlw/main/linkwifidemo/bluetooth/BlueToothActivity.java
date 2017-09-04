package com.zlw.main.linkwifidemo.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.collect.Lists;
import com.zlw.main.linkwifidemo.R;
import com.zlw.main.linkwifidemo.utils.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import top.wuhaojie.bthelper.BtHelperClient;
import top.wuhaojie.bthelper.Filter;
import top.wuhaojie.bthelper.OnSearchDeviceListener;
import top.wuhaojie.bthelper.OnSendMessageListener;

public class BlueToothActivity extends AppCompatActivity {
    private static final String TAG = BlueToothActivity.class.getSimpleName();
    @BindView(R.id.rvBluetoothList)
    RecyclerView rvBluetoothList;
    @BindView(R.id.btSend)
    Button btSend;

    private static final int startService = 0;
    private static final int msgKey = 1;
    private static final int sendOver = 2;

    private BluetoothConnectAdapter adapter;
    private List<BluetoothDevice> bluetoothDeviceList;

    private BtHelperClient btHelperClient;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private BlueToothReceiver blueToothReceiver;
    private OnSearchDeviceListener searchDeviceListener = new OnSearchDeviceListener() {
        @Override
        public void onStartDiscovery() {
            Logger.d(TAG, "开始扫描...");
        }

        @Override
        public void onNewDeviceFounded(BluetoothDevice bluetoothDevice) {
            Logger.i(TAG, "发现新的设备: " + bluetoothDevice.getName() + " " + bluetoothDevice.getAddress());
            bluetoothDeviceList.add(bluetoothDevice);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onSearchCompleted(List<BluetoothDevice> bondedList, List<BluetoothDevice> newList) {
            Logger.d(TAG, "扫描完毕 ");
            // 当搜索蓝牙设备完成后回调
            Logger.d(TAG, "----------- ");
            Logger.i(TAG, "SearchCompleted: bondedList: " + bondedList.toString());
            Logger.d(TAG, "----------- ");
            Logger.i(TAG, "SearchCompleted: newList: " + newList.toString());
            Logger.d(TAG, "----------- ");
        }

        @Override
        public void onError(Exception e) {
            Logger.printStackTrace(e);
        }
    };

    private OnSendMessageListener sendMessageListener = new OnSendMessageListener() {
        @Override
        public void onSuccess(int i, String s) {
            Logger.d(TAG, "发送成功");
        }

        @Override
        public void onConnectionLost(Exception e) {
            Logger.e(TAG, "连接失败");
            Logger.printStackTrace(e);
        }

        @Override
        public void onError(Exception e) {
            Logger.e(TAG, "发送 错误");
            Logger.printStackTrace(e);
        }
    };

    private BlueToothActionListener blueToothActionListener = new BlueToothActionListener() {
        @Override
        public void onCancle() {
            Logger.e(TAG, "取消配对");
        }

        @Override
        public void onBinding() {
            Logger.e(TAG, "配对中");
        }

        @Override
        public void onSuccess() {
            Logger.e(TAG, "配对成功");
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothChatUtil.STATE_CONNECTED:
                    String deviceName = msg.getData().getString(BluetoothChatUtil.DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "连接OK", Toast.LENGTH_SHORT).show();
                    Logger.d(TAG, "连接OK deviceName:%d", deviceName);
                    break;
                case BluetoothChatUtil.STATAE_CONNECT_FAILURE:
                    Logger.w(TAG, "连接失败");
                    Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_DISCONNECTED:
                    Logger.d(TAG, "连接断开");
                    Toast.makeText(getApplicationContext(), "连接断开", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_READ:
                    byte[] buf = msg.getData().getByteArray(BluetoothChatUtil.READ_MSG);
                    String str = new String(buf, 0, buf.length);
                    Logger.d(TAG, "获取到的数据: %s", str);
                    Toast.makeText(getApplicationContext(), "读成功" + str, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        ;
    };


    public static void startMe(Context context) {
        context.startActivity(new Intent(context, BlueToothActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(TAG, "=====================>>BlueToothActivity<<=====================");
        setContentView(R.layout.activity_blue_tooth);
        ButterKnife.bind(this);
        initRecycleView();
        init();

        //动态注册广播接收器
        Logger.d(TAG, "-----注册广播接收器：WifiReceiver-----");
        blueToothReceiver = new BlueToothReceiver(blueToothActionListener);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(blueToothReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        btHelperClient.close();
        closeBlueTooth();
    }

    @OnCheckedChanged(R.id.tbToggleBlueTooth)
    public void onChecked(boolean isChecked) {
        if (isChecked) {
            openBlueTooth();
        } else {
            closeBlueTooth();
        }
    }

    @OnClick({R.id.btScanBt, R.id.btSend, R.id.btStartService})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btScanBt:
                btHelperClient.searchDevices(searchDeviceListener);
                if (bluetoothDeviceList != null) {
                    bluetoothDeviceList.clear();
                    adapter.setNewData(bluetoothDeviceList);
                }
                break;
            case R.id.btSend:
                sendMessage();
                break;
            case R.id.btStartService:
                startBtService();
                break;
        }
    }

    private void sendMessage() {
        Logger.d(TAG, "sendMessage ...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                try {
                    BluetoothSocket socket = mBluetoothDevice.createRfcommSocketToServiceRecord(BluetoothChatUtil.MY_UUID);
                    socket.connect();
                    os = socket.getOutputStream();
                    os.write("testMessage".getBytes());
                    os.flush();
                    Logger.d(TAG, "发送 msg: testMessage");
                    mHandler.sendEmptyMessage(sendOver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startBtService() {
        Logger.d(TAG, "startBtService ...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    BluetoothServerSocket serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("serverSocket", BluetoothChatUtil.MY_UUID);
                    mHandler.sendEmptyMessage(startService);
                    BluetoothSocket accept = serverSocket.accept();
                    is = accept.getInputStream();

                    byte[] bytes = new byte[1024];
                    int length = is.read(bytes);

                    Message msg = new Message();
                    msg.what = msgKey;
                    msg.obj = new String(bytes, 0, length);
                    Logger.d(TAG, "获取到 msg: %s", (String) msg.obj);
                    Toast.makeText(BlueToothActivity.this, "获取到 msg: " + msg.obj, Toast.LENGTH_LONG).show();
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initRecycleView() {
        rvBluetoothList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BluetoothConnectAdapter();
        rvBluetoothList.setAdapter(adapter);
        bluetoothDeviceList = Lists.newArrayList();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BluetoothChatUtil.getInstance(getApplicationContext()).registerHandler(mHandler);
                BluetoothDevice bluetoothDevice = bluetoothDeviceList.get(position);
                createBond(bluetoothDevice);
                mBluetoothDevice = bluetoothDevice;
            }
        });
    }

    /**
     * 与设备配对
     */
    public void createBond(BluetoothDevice btDevice) {
        try {
            //如果想要取消已经配对的设备，只需要将creatBond改为removeBond
            Method method = BluetoothDevice.class.getMethod("createBond");
            Logger.d(TAG, "开始配对");
            method.invoke(btDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        Logger.d(TAG, "初始化蓝牙...");
        btHelperClient = BtHelperClient.from(this);
        btHelperClient.setFilter(new Filter() {
            @Override
            public boolean isCorrect(String response) {
                Logger.d(TAG, "Filter item: " + response);
                return response.trim().length() >= 5;
            }
        });
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "本地蓝牙不可用", Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "本地蓝牙不可用");
            finish();   //退出应用
        }
        String address = bluetoothAdapter.getAddress(); //获取本机蓝牙MAC地址
        String name = bluetoothAdapter.getName();   //获取本机蓝牙名称

        Logger.i(TAG, "本机蓝牙MAC地址: " + address);
        Logger.i(TAG, "本机蓝牙名称: " + name);
    }

    private void openBlueTooth() {
        Logger.d(TAG, "开启蓝牙...");
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }

    private void closeBlueTooth() {
        Logger.d(TAG, "关闭蓝牙...");
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

}

