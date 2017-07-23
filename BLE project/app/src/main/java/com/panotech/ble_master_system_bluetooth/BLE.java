package com.panotech.ble_master_system_bluetooth;

import android.util.Log;

import com.panotech.ble_master_system.R;
import com.panotech.ble_master_system_utils.LimitedSizeQueue;

import java.util.Locale;

/**
 * Created by sylar on 2017/07/17.
 */

public class BLE {
    public static final int PROXIMITY_NEAR = 1;
    public static final int PROXIMITY_IMMEDIATE = 2;
    public static final int PROXIMITY_FAR = 3;
    public static final int PROXIMITY_UNKNOWN = 0;

    final private static char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String TAG = "BLE";
    protected String proximityUuid;
    protected int major;
    protected int minor;
    protected Integer proximity;
    protected Double accuracy;
    protected int rssi;
    protected int txPower;
    protected Double runningAverageRssi = null;
    protected String Distance;

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getRssi() {
        return rssi;
    }

    public int getTxPower() {
        return txPower;
    }

    public String getProximityUuid() {
        return proximityUuid;
    }

    public void setRunningAverageRssi(Double aveRssi){
        runningAverageRssi = aveRssi;
    }

    @Override
    public int hashCode(){
        return minor;
    }

    //计算距离公式 基于rssi&txPower
    public Double getAccuracy() {
        if (accuracy == null) {
            accuracy = calculateAccuracy(txPower, runningAverageRssi != null ? runningAverageRssi : rssi);
        }
        return accuracy;
    }

//    protected static double calculateAccuracy(int txPower, double rssi) {
//        if (rssi == 0) {
//            return -1.0; // if we cannot determine accuracy, return -1.
//        }
//
//        Log.d(TAG, "calculating accuracy based on rssi of " + rssi);
//
//
//        double ratio = rssi * 1.0 / txPower;
//        if (ratio < 1.0) {
//            return Math.pow(ratio, 10);
//        } else {
//            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
//            Log.d(TAG, " avg rssi: " + rssi + " accuracy: " + accuracy);
//            return accuracy;
//        }
//    }
//...............

    protected static double calculateAccuracy(int txPower, double rssi) {
        if(rssi == 0){
            return 100.0;
        }
        else{
            return Math.pow((txPower - rssi)/20, 10);
        }
    }

    @Override
    public boolean equals(Object that) {
        if (!(that instanceof BLE)) {
            return false;
        }
        BLE thatBLE = (BLE) that;
        return (thatBLE.getMajor() == this.getMajor() && thatBLE.getMinor() == this.getMinor() && thatBLE.getProximityUuid().equals(this.getProximityUuid()));
    }

    public static BLE fromScanData(byte[] scanData, int rssi) {
        int startByte = 0;
        boolean patternFound = false;
        while (startByte <= 5) {
            //这个东西是不是需求的蓝牙设备 检测2-5位的设备标记
            if (((int)scanData[startByte] & 0xff) == 0x4c &&
                    ((int)scanData[startByte+1] & 0xff) == 0x00 &&
                    ((int)scanData[startByte+2] & 0xff) == 0x02 &&
                    ((int)scanData[startByte+3] & 0xff) == 0x15) {
                // yes!  This is an iBeacon
                patternFound = true;
                break;
            }
            else if (((int)scanData[startByte] & 0xff) == 0x2d &&
                    ((int)scanData[startByte+1] & 0xff) == 0x24 &&
                    ((int)scanData[startByte+2] & 0xff) == 0xbf &&
                    ((int)scanData[startByte+3] & 0xff) == 0x16) {
                // this is an Estimote beacon
                BLE ble = new BLE();
                ble.major = 0;
                ble.minor = 0;
                ble.proximityUuid = "00000000-0000-0000-0000-000000000000";
                ble.txPower = -55;
                return ble;
            }
            startByte++;
        }


        if (patternFound == false) {
            // This is not an iBeacon
            Log.d(TAG, "This is not a ble device (no (4c000215) seen in bytes 2-5).  The bytes I see are: "+bytesToHex(scanData));
            return null;
        }

        BLE ble = new BLE();

        ble.major = (scanData[startByte+20] & 0xff) * 0x100 + (scanData[startByte+21] & 0xff);
        ble.minor = (scanData[startByte+22] & 0xff) * 0x100 + (scanData[startByte+23] & 0xff);
        ble.txPower = (int)scanData[startByte+24]; // this one is signed
        ble.rssi = rssi;

        // AirLocate:
        // 02 01 1a 1a ff 4c 00 02 15  # Apple's fixed iBeacon advertising prefix
        // e2 c5 6d b5 df fb 48 d2 b0 60 d0 f5 a7 10 96 e0 # iBeacon profile uuid
        // 00 00 # major
        // 00 00 # minor
        // c5 # The 2's complement of the calibrated Tx Power
        // Estimote:
        // 02 01 1a 11 07 2d 24 bf 16
        // 394b31ba3f486415ab376e5c0f09457374696d6f7465426561636f6e00000000000000000000000000000000000000000000000000

        byte[] proximityUuidBytes = new byte[16];
        System.arraycopy(scanData, startByte+4, proximityUuidBytes, 0, 16);
        String hexString = bytesToHex(proximityUuidBytes);
        StringBuilder sb = new StringBuilder();
        sb.append(hexString.substring(0,8));
        sb.append("-");
        sb.append(hexString.substring(8,12));
        sb.append("-");
        sb.append(hexString.substring(12,16));
        sb.append("-");
        sb.append(hexString.substring(16,20));
        sb.append("-");
        sb.append(hexString.substring(20,32));
        ble.proximityUuid = sb.toString();

        return ble;
    }
//转化byte到hex
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    //*********

    //实际重点测试更改部分 计算圈外圈内的判定公式************************************************:
    protected static int calculateProximity(double accuracy) {
        if (accuracy > 80.0) {
            return PROXIMITY_UNKNOWN;
            // is this correct?  does proximity only show unknown when accuracy is negative?  I have seen cases where it returns unknown when
            // accuracy is -1;
        }
        if (accuracy < 80.0 && accuracy > 50.0) {
            return BLE.PROXIMITY_FAR;
        }
        // forums say 3.0 is the near/far threshold, but it looks to be based on experience that this is 4.0
        if (accuracy < 10.0) {
            return BLE.PROXIMITY_NEAR;
        }
        // if it is > 4.0 meters, call it far
        return BLE.PROXIMITY_IMMEDIATE;

    }
     //****************************:

//    protected static int calculateProximity(double accuracy) {
//        if (accuracy < 0) {
//            return PROXIMITY_UNKNOWN;
//            // is this correct?  does proximity only show unknown when accuracy is negative?  I have seen cases where it returns unknown when
//            // accuracy is -1;
//        }
//        if (accuracy < 0.5 ) {
//            return BLE.PROXIMITY_IMMEDIATE;
//        }
//        // forums say 3.0 is the near/far threshold, but it looks to be based on experience that this is 4.0
//        if (accuracy <= 4.0) {
//            return BLE.PROXIMITY_NEAR;
//        }
//        // if it is > 4.0 meters, call it far
//        return BLE.PROXIMITY_FAR;
//
//    }

//构造方法
    protected BLE(BLE otherble) {
        this.major = otherble.major;
        this.minor = otherble.minor;
        this.accuracy = otherble.accuracy;
        this.proximity = otherble.proximity;
        this.rssi = otherble.rssi;
        this.proximityUuid = otherble.proximityUuid;
        this.txPower = otherble.txPower;
    }

    protected BLE() {

    }

    protected BLE(String proximityUuid, int major, int minor, int txPower, int rssi) {
        this.proximityUuid = proximityUuid;
        this.major = major;
        this.minor = minor;
        this.rssi = rssi;
        this.txPower = txPower;
    }
    //**********************

    //输出BLE设备的信息 -----以String或者Csv的方式----- 转化成db？
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UUID=").append(this.proximityUuid.toUpperCase());
        sb.append(" Major=").append(this.major);
        sb.append(" Minor=").append(this.minor);
        sb.append(" TxPower=").append(this.txPower);

        return sb.toString();
    }

    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.proximityUuid.toUpperCase(Locale.ENGLISH)).append(",");
        sb.append(this.major).append(",");
        sb.append(this.minor).append(",");
        sb.append(this.txPower);

        return sb.toString();
    }
    //*****************************

}
