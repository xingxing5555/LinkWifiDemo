package com.zlw.main.linkwifidemo.wifi;

public class WifiConnectBean {
    private String wifiSSID;
    private String wifiPassword;
    private String wifiSecurityMode;

    public WifiConnectBean() {

    }

    public WifiConnectBean(String wifiSSID, String wifiPassword, String wifiSecurityMode) {
        setWifiSSID(wifiSSID);
        setWifiPassword(wifiPassword);
        setWifiSecurityMode(wifiSecurityMode);
    }

    public String getWifiSSID() {
        return wifiSSID;
    }

    public void setWifiSSID(String wifiSSID) {
        this.wifiSSID = wifiSSID;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public String getWifiSecurityMode() {
        return wifiSecurityMode;
    }

    public void setWifiSecurityMode(String wifiSecurityMode) {
        this.wifiSecurityMode = wifiSecurityMode;
    }

    @Override
    public String toString() {
        return "WifiConnectBean{" +
                "wifiSSID='" + wifiSSID + '\'' +
                ", wifiPassword='" + wifiPassword + '\'' +
                ", wifiSecurityMode='" + wifiSecurityMode + '\'' +
                '}';
    }
}
