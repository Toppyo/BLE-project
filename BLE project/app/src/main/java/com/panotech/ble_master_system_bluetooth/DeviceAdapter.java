package com.panotech.ble_master_system_bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.panotech.ble_master_system.R;
import com.panotech.ble_master_system_utils.DateUtil;
import com.panotech.ble_master_system_utils.ScannedDevice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sylar on 2017/07/17.
 */

public class DeviceAdapter extends ArrayAdapter<ScannedDevice> {
    private static final String PREFIX_RSSI = "RSSI:";
    private static final String PREFIX_LASTUPDATED = "Last Udpated:";
    private List<ScannedDevice> mList;
    private LayoutInflater mInflater;
    private int mResId;

    public DeviceAdapter(Context context, int resId, List<ScannedDevice> deviceList) {
        super(context, resId, deviceList);
        mResId = resId;
        mList = deviceList;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScannedDevice item = (ScannedDevice) getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(mResId, null);
        }
        if(item != null) {
            TextView name = (TextView) convertView.findViewById(R.id.device_name);
            TextView address = (TextView) convertView.findViewById(R.id.device_address);
            TextView rssi = (TextView) convertView.findViewById(R.id.device_rssi);
            TextView lastupdated = (TextView) convertView.findViewById(R.id.device_lastupdated);
            TextView scanRecord = (TextView) convertView.findViewById(R.id.device_scanrecord);
            TextView ibeaconInfo = (TextView) convertView.findViewById(R.id.device_ble_info);
            //Resources res = convertView.getContext().getResources();

            if(name != null){ name.setText(item.getDisplayName()); }
            if(address != null){ address.setText(item.getDevice().getAddress()); }
            if(rssi != null){ rssi.setText(Integer.toString(item.getRssi())); }
            if(lastupdated != null){ lastupdated.setText(PREFIX_LASTUPDATED + DateUtil.get_yyyyMMddHHmmssSSS(item.getLastUpdatedMs())); }
            if(scanRecord != null){ scanRecord.setText(item.getScanRecordHexString()); }
            if (item.getBLE() != null) {
                ibeaconInfo.setText(/*res.getString(R.string.label_ble)*/"This is my BLE" + "\n"
                        + item.getBLE().toString());
            } else {
                ibeaconInfo.setText(/*res.getString(R.string.label_not_ble)*/ "This is not my BLE");
            }

        }

        return convertView;
    }

    /**
     * add or update BluetoothDevice List
     *
     * 新扫描的BLE + 新的rssi + 接受到的蓝牙包数据
     * 返回数据 ex. "BLE:3 (Total:10)"
     */
    public String update(BluetoothDevice newDevice, int rssi, byte[] scanRecord) {
        if ((newDevice == null) || (newDevice.getAddress() == null)) {
            return "";
        }
        long now = System.currentTimeMillis();

        boolean contains = false;
        for (ScannedDevice device : mList) {
            if (newDevice.getAddress().equals(device.getDevice().getAddress())) {
                contains = true;
                // update
                device.setRssi(rssi);
                device.setLastUpdatedMs(now);
                device.setScanRecord(scanRecord);
                break;
            }
        }
        if (!contains) {
            // add new BluetoothDevice
            mList.add(new ScannedDevice(newDevice, rssi, scanRecord, now));
        }

        // sort by RSSI
        Collections.sort(mList, new Comparator<ScannedDevice>() {
            @Override
            public int compare(ScannedDevice lhs, ScannedDevice rhs) {
                if(lhs.getBLE() != null && rhs.getBLE() == null){
                    return -1;
                }
                else if(lhs.getBLE() == null && rhs.getBLE() != null){
                    return 1;
                }
                else {
                    if (lhs.getRssi() == 0) {
                        return 1;
                    } else if (rhs.getRssi() == 0) {
                        return -1;
                    }
                    if (lhs.getRssi() > rhs.getRssi()) {
                        return -1;
                    } else if (lhs.getRssi() < rhs.getRssi()) {
                        return 1;
                    }
                    return 0;
                }
            }
        });

        notifyDataSetChanged();

        // create summary
        int totalCount = 0;
        int BLECount = 0;
        if (mList != null) {
            totalCount = mList.size();
            for (ScannedDevice device : mList) {
                if (device.getBLE() != null) {
                    BLECount++;
                }
            }
        }
//        String summary = "BLE:" + Integer.toString(BLECount) + " (Total:"
//                + Integer.toString(totalCount) + ")";

        String summary = Integer.toString(BLECount) + ":" + Integer.toString(totalCount);
        return summary;
    }

    public List<ScannedDevice> getList() {
        return mList;
    }
}
