package com.panotech.ble_master_system;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panotech.ble_master_system_bluetooth.DAdapter;
import com.panotech.ble_master_system_bluetooth.DeviceAdapter;
import com.panotech.ble_master_system_bluetooth.ScannedDevice;
import com.panotech.ble_master_system_utils.PropertyUtil;

import org.apache.commons.io.FileUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import static com.panotech.ble_master_system_bluetooth.ScannedDevice.asHex;

/**
 * Created by sylar on 2017/07/23.
 */

public class TestSettingsActivity extends AppCompatActivity {
    private DAdapter mDAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Button mScanButton, mResetButton, mSaveButton;
    private boolean mScanning;;
    public static final int REQUEST = 100;
    private static final String PROPERTIES_PATH = "/ble_master_system_utils/";

    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mScanButton = (Button)findViewById(R.id.btn_settings_scan);
        mResetButton = (Button)findViewById(R.id.btn_settings_reset);
        mSaveButton = (Button)findViewById(R.id.btn_settings_save);

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

            mScanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Starting scan", Toast.LENGTH_LONG).show();
                    startScanBLE();
                }
            });

            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveCustomersInformations();
                    Toast.makeText(getApplicationContext(), "Saving success!", Toast.LENGTH_LONG).show();
                    Properties properties = new Properties();
                    InputStream input = null;
                    try {
                        input = new FileInputStream("BLE.properties");//加载Java项目根路径下的配置文件
                        properties.load(input);// 加载属性文件
                    } catch (IOException io) {
                    } finally {
                        if (input != null) {
                            try {
                                input.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Log.d("properties saved:", properties.getProperty("1") + properties.getProperty("2") + properties.getProperty("3"));
                }
            });

            mResetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    deleteFile("config.properties");
                    Properties properties = new Properties();
                    InputStream input = null;
                    try {
                        input = new FileInputStream("BLE.properties");//加载Java项目根路径下的配置文件
                        properties.load(input);// 加载属性文件
                    } catch (IOException io) {
                    } finally {
                        if (input != null) {
                            try {
                                input.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mResetButton.setText(properties.getProperty("3"));
                }
            });
        }


    }

    private BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String struuid = asHex(scanRecord);
                    String summary = mDAdapter.update(device, rssi, scanRecord);
                    mDAdapter.getList();//addDevice(device);
                    mDAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private void startScanBLE() {
        mBluetoothAdapter.startLeScan(scanCallback);
        mScanning = true;
        ListView deviceListView = (ListView) findViewById(R.id.listview_settings);
        mDAdapter = new DAdapter(this, R.layout.list_settings,
                new ArrayList<ScannedDevice>());
        try {
            Thread.sleep(3000);
            deviceListView.setAdapter(mDAdapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveCustomersInformations(){
        ListView list= (ListView)findViewById(R.id.listview_settings);//获得listview
        for (int i = 0; i < list.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout) list.getChildAt(i);// 获得子item的layout
            EditText tName = (EditText) layout.findViewById(R.id.textview_settings_name);// 从layout中获得控件,根据其id
            EditText tSeat = (EditText) layout.findViewById(R.id.textview_settings_seat);
            EditText tAppear = (EditText) layout.findViewById(R.id.textview_settings_appear);
            mDAdapter.updateCustomers(i, tName.getText().toString(), tSeat.getText().toString(), tAppear.getText().toString());
        }
        mDAdapter.updateProperties();
    }

    public void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("BLE.properties");//加载Java项目根路径下的配置文件
            properties.load(input);// 加载属性文件
        } catch (IOException io) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
