package com.panotech.ble_master_system_utils;

import android.os.Environment;
import android.util.Log;

import com.panotech.ble_master_system_bluetooth.ScannedDevice;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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

    public String getCsvString(int row,int col, String path){
        String last = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));//换成你的文件名
            // reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉
            String line = null;
            int index=0;
            while((line=reader.readLine())!=null){
                String item[] = line.split(" ");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
                if(index==row-1){
                    if(item.length>=col-1){
                        last = item[col-1];//这就是你要的数据了
                    }
                }
                //int value = Integer.parseInt(last);//如果是数值，可以转化为数值
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return last;
        }
    }
}
