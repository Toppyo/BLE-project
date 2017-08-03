package com.panotech.ble_master_system;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.panotech.ble_master_system_bluetooth.CommonData;
import com.panotech.ble_master_system_bluetooth.DeviceAdapter;
import com.panotech.ble_master_system_bluetooth.ScannedDevice;
import com.panotech.ble_master_system_utils.DateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.panotech.ble_master_system.TestBLE.REQUEST;
import static com.panotech.ble_master_system_bluetooth.ScannedDevice.asHex;


public class MainActivity extends Activity {
    private Button mButtonCheck, mButtonCheckAbsence, mButtonSettings, mButtonBLETest;
    private TextView mTextViewTourName, mTextViewPeopleCounting, mTextViewAppointmentTime;
    public static String CSVFILE;
    private BluetoothAdapter mBluetoothAdapter;

    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, "")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            if (CommonData.isOver()) {
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonCheck=(Button)findViewById(R.id.btn_check);
        mButtonCheckAbsence=(Button)findViewById(R.id.btn_check_absence);
        mButtonSettings=(Button)findViewById(R.id.btn_settings);
        mButtonBLETest=(Button)findViewById(R.id.btn_ble_test);
        mTextViewTourName = (EditText)findViewById(R.id.main_tour_input);
        mTextViewPeopleCounting = (EditText)findViewById(R.id.main_people_counting);
        mTextViewAppointmentTime = (EditText)findViewById(R.id.main_appointment_time);

        if(Visitors.settingsMap.containsKey("tour")){
            mTextViewTourName.setText(Visitors.settingsMap.get("tour"));
        }
        if(Visitors.settingsMap.containsKey("people")){
            mTextViewPeopleCounting.setText(Visitors.settingsMap.get("people"));
        }
        if(Visitors.settingsMap.containsKey("time")){
            mTextViewAppointmentTime.setText(Visitors.settingsMap.get("time"));
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this,"Manifest.permission.ACCESS_COARSE_LOCATION")
                != PackageManager.PERMISSION_GRANTED)
        {
            //注意第二个参数没有双引号
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }

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

        try {
            CommonData.mDeviceAdapter = new DeviceAdapter(this, R.layout.list_ble,
                    new ArrayList<ScannedDevice>());
            mBluetoothAdapter.startLeScan(scanCallback);
        }catch (Exception e){
            e.printStackTrace();
        }

        mButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, CheckActivity.class);
                startActivity(intent1);
            }
        });
        mButtonCheckAbsence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, CheckAbsenceActivity.class);
                startActivity(intent2);
            }
        });
        mButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this, TestSettingsActivity.class);
                startActivity(intent3);
            }
        });

        mButtonBLETest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(MainActivity.this, TestShowLogActivity.class);
                startActivity(intent4);
            }
        });

        //final Handler handler = new Handler(){
            //@Override
            //public void handleMessage(Message msg){
                //super.handleMessage(msg);
                //if(msg.what == 1){
//                    mTextViewTourName = (TextView)findViewById(R.id.tour_input);
//                    mTextViewPeopleCounting = (TextView)findViewById(R.id.people_counting);
//                    mTextViewAppointmentTime = (TextView)findViewById(R.id.appointment_time);
//                    mTextViewTourName.setText("ぶらり京都観光バスの旅:No1");
//                    mTextViewPeopleCounting.setText("36人");
//                    mTextViewAppointmentTime.setText("8時間");
                //}
            //}
        //};

        /*final Thread updateUI = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        });

        mButtonMainFresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI.start();
            }
        });*/

    }

    private BluetoothAdapter.LeScanCallback scanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String struuid = asHex(scanRecord);
                    Log.i("Searching BLE devices", "device=" + device.getName() + ";rssi=" + rssi + ";scanRecord=" + struuid);
                    String summary = CommonData.mDeviceAdapter.update(device, rssi, scanRecord);
                    Log.i("SUMMARY----------", summary);
                    Visitors.settingsMap.put("summary", summary);
                    List<ScannedDevice> list = CommonData.mDeviceAdapter.getList();//addDevice(device);
                    CommonData.mDeviceAdapter.notifyDataSetChanged();
                    //For test log using
//                    long now = System.currentTimeMillis();
//                    ScannedDevice scannedDevice = new ScannedDevice(device, rssi, scanRecord, now);
//                    if(scannedDevice.getBLE() != null){
//                        String logData = scannedDevice.getBLE().toString() + "  " + rssi + "  " + scannedDevice.timeToString();
//                        CommonData.TestLogQueue.add(logData);
//                        Log.i("LOGDATA", logData);
//                    }
//                    for(ScannedDevice device : list){
//                        if(now - device.getLogLastUpdatedMs() > 1000){
//                            String logData = "******" + device.getBLE().toString() + "  AveDistance:"
//                                    + String.valueOf(device.getAveAccuracy()) + "  " + DateUtil.get_yyyyMMddHHmmssSSS(now);
//                            device.setLogLastUpdatedMs(now);
//                            CommonData.TestLogQueue.add(logData);
//                        }
//                    }
                }
            });
        }
    };

    @Override
    protected void onPause(){
        super.onPause();
        String tour = mTextViewTourName.getText().toString();
        String people = mTextViewPeopleCounting.getText().toString();
        String time = mTextViewAppointmentTime.getText().toString();
        Visitors.settingsMap.put("tour", tour);
        Visitors.settingsMap.put("people", people);
        Visitors.settingsMap.put("time", time);
    }
}
