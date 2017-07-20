package com.panotech.ble_master_system_utils;

import android.bluetooth.BluetoothDevice;
import android.widget.LinearLayout;

import com.panotech.ble_master_system_bluetooth.BLE;

/**
 * Created by sylar on 2017/07/17.
 */

public class ScannedDevice {
    private static final String UNKNOWN = "Unknown";
    private BluetoothDevice mDevice;
    private int mRssi;
    private String mDisplayName;
    private byte[] mScanRecord;
    private BLE mBLE;
    private long mLastUpdatedMs;

    public ScannedDevice(BluetoothDevice device, int rssi, byte[] scanRecord, long now) {
        if (device == null) {
            throw new IllegalArgumentException("BluetoothDevice is null");
        }
        mLastUpdatedMs = now;
        mDevice = device;
        mDisplayName = device.getName();
        if ((mDisplayName == null) || (mDisplayName.length() == 0)) {
            mDisplayName = UNKNOWN;
        }
        mRssi = rssi;
        mScanRecord = scanRecord;
        checkIBeacon();
    }

    private void checkIBeacon() {
        if (mScanRecord != null) {
            mBLE = BLE.fromScanData(mScanRecord, mRssi);
        }
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public int getRssi() {
        return mRssi;
    }

    public void setRssi(int rssi) {
        mRssi = rssi;
    }

    public long getLastUpdatedMs() {
        return mLastUpdatedMs;
    }

    public void setLastUpdatedMs(long lastUpdatedMs) {
        mLastUpdatedMs = lastUpdatedMs;
    }

    public byte[] getScanRecord() {
        return mScanRecord;
    }

    public String getScanRecordHexString() {
        return ScannedDevice.asHex(mScanRecord);
    }

    public void setScanRecord(byte[] scanRecord) {
        mScanRecord = scanRecord;
        checkIBeacon();
    }

    public BLE getBLE() {
        return mBLE;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    //输出扫描设备的Csv文件   ----db替换----
    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        // DisplayName,MAC Addr,RSSI,Last Updated,iBeacon flag,Proximity UUID,major,minor,TxPower
        sb.append(mDisplayName).append(",");
        sb.append(mDevice.getAddress()).append(",");
        sb.append(mRssi).append(",");
        sb.append(DateUtil.get_yyyyMMddHHmmssSSS(mLastUpdatedMs)).append(",");
        if (mBLE == null) {
            sb.append("false,,0,0,0");
        } else {
            sb.append("true").append(",");
            sb.append(mBLE.toCsv());
        }
        return sb.toString();
    }

    //一种加密 将输入的byte前十位加零间隔排列
    public static String asHex(byte bytes[]) {
        if ((bytes == null) || (bytes.length == 0)) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int index = 0; index < bytes.length; index++) {
            int bt = bytes[index] & 0xff;
            if (bt < 0x10) {
                sb.append("0");
            }

            sb.append(Integer.toHexString(bt).toUpperCase());
        }

        return sb.toString();
    }

}
