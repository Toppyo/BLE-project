package com.panotech.ble_master_system;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panotech.ble_master_system_bluetooth.DeviceAdapter;
import com.panotech.ble_master_system_utils.ScannedDevice;

import java.util.ArrayList;

import static com.panotech.ble_master_system_utils.ScannedDevice.asHex;

/**
 * Created by sylar on 2017/07/19.
 */

public class TestBLE extends Activity {
    private DeviceAdapter mDeviceAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Button mRefreshButton;
    private boolean mScanning;
    private TextView mCheckPeopleTextView;
    public static final int REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ble_activity);
        mScanning = false;
        mCheckPeopleTextView = (TextView)findViewById(R.id.test_check_people);
        mRefreshButton = (Button)findViewById(R.id.btn_test_refresh);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No support for BLE on this device", Toast.LENGTH_SHORT).show();
        } else {
            BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            //获得蓝牙适配器
            mBluetoothAdapter = manager.getAdapter();
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST);
            }

        }

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOrStopSearchBLE(mScanning);
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(mScanning){
            mBluetoothAdapter.stopLeScan(scanCallback);
            mScanning = false;
        }
    }

    private BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String struuid = asHex(scanRecord);
                    Log.i("Searching BLE devices", "device=" + device.getName() + ";rssi=" + rssi + ";scanRecord=" + struuid);

                    String summary = mDeviceAdapter.update(device, rssi, scanRecord);
                    if (summary != null) {
                        mCheckPeopleTextView.setText(summary);
                    }
                    mDeviceAdapter.getList();//addDevice(device);
                    mDeviceAdapter.notifyDataSetChanged();
                }
            });
        }
    };



    private void startOrStopSearchBLE(boolean Scanning) {
        if (!Scanning) {
            mBluetoothAdapter.startLeScan(scanCallback);
            mScanning = true;
            ListView deviceListView = (ListView) findViewById(R.id.test_list_view);
            mDeviceAdapter = new DeviceAdapter(this, R.layout.list_ble,
                    new ArrayList<ScannedDevice>());
            deviceListView.setAdapter(mDeviceAdapter);
        } else {
            mBluetoothAdapter.stopLeScan(scanCallback);
            mScanning = false;
        }
    }
}
