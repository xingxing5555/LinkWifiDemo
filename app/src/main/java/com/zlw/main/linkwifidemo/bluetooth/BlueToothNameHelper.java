package com.zlw.main.linkwifidemo.bluetooth;


public class BlueToothNameHelper {
    public static final int AUDIO_VIDEO_CAMCORDER = 1076; // 音视频摄像机
    public static final int AUDIO_VIDEO_CAR_AUDIO = 1056;
    public static final int AUDIO_VIDEO_HANDSFREE = 1032;
    public static final int AUDIO_VIDEO_HEADPHONES = 1048;// 头戴式受话器
    public static final int AUDIO_VIDEO_HIFI_AUDIO = 1064;
    public static final int AUDIO_VIDEO_LOUDSPEAKER = 1044; //  扬声器
    public static final int AUDIO_VIDEO_MICROPHONE = 1040; //  麦克风
    public static final int AUDIO_VIDEO_PORTABLE_AUDIO = 1052;// 手提..
    public static final int AUDIO_VIDEO_SET_TOP_BOX = 1060;
    public static final int AUDIO_VIDEO_UNCATEGORIZED = 1024;
    public static final int AUDIO_VIDEO_VCR = 1068; //VCR
    public static final int AUDIO_VIDEO_VIDEO_CAMERA = 1072;
    public static final int AUDIO_VIDEO_VIDEO_CONFERENCING = 1088;
    public static final int AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER = 1084;
    public static final int AUDIO_VIDEO_VIDEO_GAMING_TOY = 1096;
    public static final int AUDIO_VIDEO_VIDEO_MONITOR = 1080;//监视器
    public static final int AUDIO_VIDEO_WEARABLE_HEADSET = 1028;// 可穿戴耳机
    public static final int COMPUTER_DESKTOP = 260;//电脑桌面
    public static final int COMPUTER_HANDHELD_PC_PDA = 272;//掌上电脑PAD
    public static final int COMPUTER_LAPTOP = 268;//便携式电脑(笔记本)

    public static final int COMPUTER_PALM_SIZE_PC_PDA = 276;//PDA
    public static final int COMPUTER_SERVER = 264;
    public static final int COMPUTER_WEARABLE = 280;//可穿戴电脑
    public static final int HEALTH_BLOOD_PRESSURE = 2308;//健康设备,血压器
    public static final int HEALTH_DATA_DISPLAY = 2332;//健康设备,数据展示
    public static final int HEALTH_GLUCOSE = 2320;//葡萄糖
    public static final int HEALTH_PULSE_OXIMETER = 2324;//脉搏仪
    public static final int HEALTH_PULSE_RATE = 2328;//脉搏率
    public static final int HEALTH_THERMOMETER = 2312;//温度计
    public static final int HEALTH_WEIGHING = 2316;
    public static final int PHONE_CELLULAR = 516;//蜂窝电话
    public static final int PHONE_CORDLESS = 520;//无线电话
    public static final int PHONE_ISDN = 532;//ISDN电话
    public static final int PHONE_MODEM_OR_GATEWAY = 528;
    public static final int PHONE_SMART = 524;//智能手机
    public static final int TOY_CONTROLLER = 2064;
    public static final int TOY_DOLL_ACTION_FIGURE = 2060;
    public static final int TOY_GAME = 2068;
    public static final int TOY_ROBOT = 2052;
    public static final int TOY_VEHICLE = 2056;
    public static final int WEARABLE_GLASSES = 1812;
    public static final int WEARABLE_HELMET = 1808;
    public static final int WEARABLE_JACKET = 1804;
    public static final int WEARABLE_PAGER = 1800;
    public static final int WEARABLE_WRIST_WATCH = 1796;

    //android 6.0 Api
    public static final int PHONE = 512;//手机
    public static final int COMPUTER = 256;//电脑
    public static final int HEALTH = 2304;//健康设备
    public static final int IMAGING = 1536;//显示设备
    public static final int MISC = 0;//混合
    public static final int NETWORKING = 768;//通信设备
    public static final int PERIPHERAL = 1280;//外部设备
    public static final int TOY = 2048;//玩具
    public static final int UNCATEGORIZED = 7936;//未分类
    public static final int WEARABLE = 1792;//可穿戴设备

    public static final int BOND_NONE = 10;
    public static final int BOND_BONDING = 11;
    public static final int BOND_BONDED = 12;

    public static String getDeviceName(int typeCode) {
        switch (typeCode) {
            case PHONE:
                return "手机";
            case COMPUTER:
                return "电脑";
            case UNCATEGORIZED:
                return "未分类";
            case HEALTH:
                return "健康设备";
            case IMAGING:
                return "显示设备";
            case MISC:
                return "混合";
            case NETWORKING:
                return "通信设备";
            case TOY:
                return "玩具";
            case WEARABLE:
                return "外部设备";
            case PERIPHERAL:
                return "可穿戴设备";

            case AUDIO_VIDEO_CAMCORDER:
                return "音视频摄像机";
            case AUDIO_VIDEO_HEADPHONES:
                return "头戴式受话器";
            case AUDIO_VIDEO_LOUDSPEAKER:
                return "扬声器";
            case AUDIO_VIDEO_MICROPHONE:
                return "麦克风";
            case AUDIO_VIDEO_PORTABLE_AUDIO:
                return "手提式设备";
            case AUDIO_VIDEO_VIDEO_MONITOR:
                return "监视器";
            case AUDIO_VIDEO_WEARABLE_HEADSET:
                return "可穿戴耳机";
            case COMPUTER_DESKTOP:
                return "电脑桌面";
            case COMPUTER_HANDHELD_PC_PDA:
                return "掌上电脑PAD";
            case COMPUTER_LAPTOP:
                return "便携式电脑(笔记本)";
            case COMPUTER_PALM_SIZE_PC_PDA:
                return "PDA";
            case COMPUTER_WEARABLE:
                return "可穿戴电脑";
            case HEALTH_BLOOD_PRESSURE:
                return "健康设备,血压器";
            case HEALTH_DATA_DISPLAY:
                return "健康设备,数据展示";
            case HEALTH_GLUCOSE:
                return "葡萄糖";
            case HEALTH_PULSE_OXIMETER:
                return "脉搏仪";
            case HEALTH_PULSE_RATE:
                return "脉搏率";
            case HEALTH_THERMOMETER:
                return "温度计";
            case PHONE_CELLULAR:
                return "蜂窝电话";
            case PHONE_CORDLESS:
                return "无线电话";
            case PHONE_ISDN:
                return "ISDN电话";
            case PHONE_MODEM_OR_GATEWAY:
                return "智能手机";
            default:
                return "其他设备: " + typeCode;
        }
    }

    public static String getStatusName(int statusCode) {
        switch (statusCode) {
            case BOND_NONE:
                return "未进行配对";
            case BOND_BONDED:
                return "已配对";
            case BOND_BONDING:
                return "配对中";
            default:
                return "未知状态: " + statusCode;
        }
    }
}
