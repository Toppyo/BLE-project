package com.panotech.ble_master_system_utils;

import android.os.Environment;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.apache.http.protocol.HTTP;

/**
 * Created by sylar on 2017/07/17.
 */

public class CsvDumpUtil {
    private static final String TAG = CsvDumpUtil.class.getSimpleName();
    private static final String HEADER = "DisplayName,MAC Addr,RSSI,Last Updated,iBeacon flag,Proximity UUID,major,minor,TxPower";
    private static final String DUMP_PATH = "/ble_master_system_utils/";

    public static String dump(List<ScannedDevice> deviceList) {
        FileOutputStream fos = null;
        PrintWriter pw = null;
        //设定储存地址path
        if ((deviceList == null) || (deviceList.size() == 0)) {
            return null;
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + DUMP_PATH
                + DateUtil.get_nowCsvFilename();
        Log.d(TAG, "dump path=" + path);
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER).append("\n");
        for (ScannedDevice device : deviceList) {
            sb.append(device.toCsv()).append("\n");
        }

        try {
            FileUtils.write(new File(path), sb.toString(), HTTP.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return path;

    }
}
