package com.panotech.ble_master_system_utils;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by sylar on 2017/07/17.
 */

public class BleUtil {
    private BleUtil(){

    }

    public static boolean isBLESupported(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public static BluetoothManager getManager(Context context){
        return (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
    }
}
