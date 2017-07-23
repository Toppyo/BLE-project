package com.panotech.ble_master_system_bluetooth;

import android.bluetooth.BluetoothDevice;

import com.panotech.ble_master_system_utils.DateUtil;
import com.panotech.ble_master_system_utils.LimitedSizeQueue;
import com.panotech.ble_master_system_webconnect.Customer;
import com.panotech.ble_master_system_webconnect.CustomerLab;

import java.util.UUID;

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
    private Double aveRssi;
    private UUID mUUID;
    private String mName, mSeat, mAppear;

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
        mUUID = UUID.randomUUID();
        mName = "";
        mSeat = "";
        mAppear = "";
        checkIBeacon();
    }

    private void checkIBeacon() {
        if (mScanRecord != null) {
            mBLE = BLE.fromScanData(mScanRecord, mRssi);
        }
    }

    public UUID getUUID() {
        return mUUID;
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

    public Double getAveRssi() {
        aveRssi = calculateAveRssi(rssiStore);
        return aveRssi;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSeat() {
        return mSeat;
    }

    public void setSeat(String seat) {
        mSeat = seat;
    }

    public String getAppear() {
        return mAppear;
    }

    public void setAppear(String appear) {
        mAppear = appear;
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

    public LimitedSizeQueue<Integer> rssiStore = new LimitedSizeQueue(5);

    private Double calculateAveRssi(LimitedSizeQueue<Integer> rssiStore){
        int i = rssiStore.size();
        int sum = 0;
        for(int rssi : rssiStore){
            sum += rssi;
        }
        return sum*(1.0)/i;
    }
}
