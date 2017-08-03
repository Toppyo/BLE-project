package com.panotech.ble_master_system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.panotech.ble_master_system_bluetooth.BLE;
import com.panotech.ble_master_system_bluetooth.CommonData;
import com.panotech.ble_master_system_bluetooth.DAdapter;
import com.panotech.ble_master_system_bluetooth.DeviceAdapter;
import com.panotech.ble_master_system_bluetooth.ScannedDevice;
import com.panotech.ble_master_system_utils.PropertyUtil;

import org.apache.commons.io.FileUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import static com.panotech.ble_master_system_bluetooth.BLE.calculateAccuracy;
import static com.panotech.ble_master_system_bluetooth.BLE.calculateProximity;
import static com.panotech.ble_master_system_bluetooth.ScannedDevice.asHex;

/**
 * Created by sylar on 2017/07/23.
 */

public class TestSettingsActivity extends Activity {
    private DAdapter mDAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Button mScanButton, mResetButton, mSaveButton;
    private boolean mScanning;;
    public static final int REQUEST = 100;
    private static final String PROPERTIES_PATH = "/ble_master_system_utils/";

//    private static final ArrayList<String> uuidList = new ArrayList<String>(Arrays.asList(
//            "4241309F-4F2E-4DA5-8ACA-3BA39E47D62E",
//            "48534442-4C45-4144-80C0-180000000007",
//            "48534442-4C45-4144-80C0-180000000006"
//    ));
    private static final ArrayList<String> uuidList =new ArrayList<>();

    private Timer mTimer;
    private Handler mHandler;

    // 基準調整
    private static Integer bleStandardId = R.id.spinner_ble_standard;

    private static ArrayList<Integer> majorList = new ArrayList<>();
    private static ArrayList<Integer> minorList = new ArrayList<>();
    private static ArrayList<Integer> distantsList = new ArrayList<>();
    private static ArrayList<Integer> namesList = new ArrayList<>();
    private static ArrayList<Integer> seatsList = new ArrayList<>();
    private static ArrayList<Integer> featuresList = new ArrayList<>();

    static {
        majorList.add(R.id.textview_settings_major1);
        majorList.add(R.id.textview_settings_major2);
        majorList.add(R.id.textview_settings_major3);
        majorList.add(R.id.textview_settings_major4);
        majorList.add(R.id.textview_settings_major5);
        majorList.add(R.id.textview_settings_major6);
        majorList.add(R.id.textview_settings_major7);
        majorList.add(R.id.textview_settings_major8);
        majorList.add(R.id.textview_settings_major9);
        majorList.add(R.id.textview_settings_major10);

        minorList.add(R.id.textview_settings_minor1);
        minorList.add(R.id.textview_settings_minor2);
        minorList.add(R.id.textview_settings_minor3);
        minorList.add(R.id.textview_settings_minor4);
        minorList.add(R.id.textview_settings_minor5);
        minorList.add(R.id.textview_settings_minor6);
        minorList.add(R.id.textview_settings_minor7);
        minorList.add(R.id.textview_settings_minor8);
        minorList.add(R.id.textview_settings_minor9);
        minorList.add(R.id.textview_settings_minor10);

        distantsList.add(R.id.textview_settings_distance1);
        distantsList.add(R.id.textview_settings_distance2);
        distantsList.add(R.id.textview_settings_distance3);
        distantsList.add(R.id.textview_settings_distance4);
        distantsList.add(R.id.textview_settings_distance5);
        distantsList.add(R.id.textview_settings_distance6);
        distantsList.add(R.id.textview_settings_distance7);
        distantsList.add(R.id.textview_settings_distance8);
        distantsList.add(R.id.textview_settings_distance9);
        distantsList.add(R.id.textview_settings_distance10);

        namesList.add(R.id.textview_settings_name1);
        namesList.add(R.id.textview_settings_name2);
        namesList.add(R.id.textview_settings_name3);
        namesList.add(R.id.textview_settings_name4);
        namesList.add(R.id.textview_settings_name5);
        namesList.add(R.id.textview_settings_name6);
        namesList.add(R.id.textview_settings_name7);
        namesList.add(R.id.textview_settings_name8);
        namesList.add(R.id.textview_settings_name9);
        namesList.add(R.id.textview_settings_name10);

        seatsList.add(R.id.spinner_settings_seat1);
        seatsList.add(R.id.spinner_settings_seat2);
        seatsList.add(R.id.spinner_settings_seat3);
        seatsList.add(R.id.spinner_settings_seat4);
        seatsList.add(R.id.spinner_settings_seat5);
        seatsList.add(R.id.spinner_settings_seat6);
        seatsList.add(R.id.spinner_settings_seat7);
        seatsList.add(R.id.spinner_settings_seat8);
        seatsList.add(R.id.spinner_settings_seat9);
        seatsList.add(R.id.spinner_settings_seat10);


        featuresList.add(R.id.spinner_settings_appear1);
        featuresList.add(R.id.spinner_settings_appear2);
        featuresList.add(R.id.spinner_settings_appear3);
        featuresList.add(R.id.spinner_settings_appear4);
        featuresList.add(R.id.spinner_settings_appear5);
        featuresList.add(R.id.spinner_settings_appear6);
        featuresList.add(R.id.spinner_settings_appear7);
        featuresList.add(R.id.spinner_settings_appear8);
        featuresList.add(R.id.spinner_settings_appear9);
        featuresList.add(R.id.spinner_settings_appear10);

    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try {
            if (CommonData.isOver()) {
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_settings);

        mScanButton = (Button)findViewById(R.id.btn_settings_scan);
        mResetButton = (Button)findViewById(R.id.btn_settings_reset);
        mSaveButton = (Button)findViewById(R.id.btn_settings_save);

        int pos = 0;
        List<ScannedDevice> mDevices = CommonData.mDeviceAdapter.getList();
        while (pos < mDevices.size()){
            ScannedDevice device = mDevices.get(pos++);
            if(device.getBLE() != null){
                String uuid = device.getBLE().getProximityUuid().toLowerCase();
                byte[] bytes = uuid.getBytes();
                if((int)bytes[0] < 97) {
                    uuidList.add(device.getBLE().getProximityUuid().toUpperCase());
                }
            }
        }

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                List<ScannedDevice> tempList = CommonData.mDeviceAdapter.getList();
                if (tempList != null && tempList.size() > 0) {
                    for (int i = 0; i < tempList.size(); i++) {
                        ScannedDevice dev = tempList.get(i);
                        Log.i("UUID is ------------", dev.getBLE().getProximityUuid().toUpperCase());

                        if (!uuidList.get(0).equals(dev.getBLE().getProximityUuid().toUpperCase())){
                            continue;
                        }

                        TextView ma =  (TextView)findViewById(majorList.get(i));
                        String maStr = String.valueOf(dev.getBLE().getMajor());
                        ma.setText(maStr);

                        TextView mi =  (TextView)findViewById(minorList.get(i));
                        String miStr = String.valueOf(dev.getBLE().getMinor());
                        mi.setText(miStr);

                        double a = dev.getAveAccuracy();
                        TextView dis =  (TextView)findViewById(distantsList.get(i));
                        String disStr = calculateDistance(a);
                        Log.i("SETdistance", disStr + dev.timeToString());
                        dis.setText(disStr);
                    }
                }

            }
        };

        Spinner stan = (Spinner)findViewById(R.id.spinner_ble_standard);
        stan.setSelection(0);


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No support for BLE on this device", Toast.LENGTH_SHORT).show();
        } else {
            BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            //获得蓝牙适配器
//            mBluetoothAdapter = manager.getAdapter();
//            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(intent, REQUEST);
//            }

            mScanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), getText(R.string.settings_startscan), Toast.LENGTH_SHORT).show();
//                    startScanBLE();

                    List<ScannedDevice> list = CommonData.mDeviceAdapter.getList();
                    Spinner standard = (Spinner)findViewById(R.id.spinner_ble_standard);
                    standard.setSelection(Visitors.getBle_standard());

                    TextView uuidTextView =  (TextView)findViewById(R.id.textview_settings_uuid);
                    Log.i("LISTSIZE", Integer.toString(list.size()));
                    if (list != null && list.size() > 0 && uuidList.get(0) != null) {

                        Log.i("Searching BLE devices", "size=" + list.size() );
                        ScannedDevice deviceFirst = list.get(0);
                        String uuid = deviceFirst.getBLE().getProximityUuid();
                        Log.i("uuid length", "length=" + uuid.length() );

                        uuidTextView.setText(uuidList.get(0));

                        List<ScannedDevice> myDeviceList = new ArrayList<>();
                        boolean contain;
                        for(ScannedDevice device : list){
                            if (uuidList.get(0).equals(device.getBLE().getProximityUuid().toUpperCase())){
                                contain = false;
                                for(ScannedDevice device1 : myDeviceList){
                                    if(device.getDevice().getAddress().equals(device1.getDevice().getAddress())) contain = true;
                                }
                                if(!contain) {
                                    myDeviceList.add(device);
                                }
                            }
                        }

                        for (int i = 0; i < myDeviceList.size(); i++) {
                            ScannedDevice device = myDeviceList.get(i);

                            TextView major =  (TextView)findViewById(majorList.get(i));
                            String majorStr = String.valueOf(device.getBLE().getMajor());
                            major.setText(majorStr);

                            TextView minor =  (TextView)findViewById(minorList.get(i));
                            String minorStr = String.valueOf(device.getBLE().getMinor());
                            minor.setText(minorStr);

                            double a = device.getAveAccuracy();
                            TextView distance =  (TextView)findViewById(distantsList.get(i));
                            distance.setText(calculateDistance(a));

                            if (Visitors.visitormap.isEmpty() || Visitors.visitormap.size() == 0){
                                continue;
                            } else {
                                EditText name =  (EditText) findViewById(namesList.get(i));
                                String strName = name.getText().toString();

                                Spinner seat =  (Spinner) findViewById(seatsList.get(i));
                                String strSeat = seat.getSelectedItem().toString();

                                Spinner feature = (Spinner)findViewById(featuresList.get(i));
                                String strFeature = feature.getSelectedItem().toString();

                                if (Visitors.visitormap.get(majorStr) != null && Visitors.visitormap.get(majorStr).get(minorStr) != null){
                                    Visitor visitor = Visitors.visitormap.get(majorStr).get(minorStr);
                                    name.setText(visitor.name);
                                    seat.setSelection(visitor.getSeatID());

                                    feature.setSelection(visitor.getFeatureID());
                                }
                            }

                        }

                    }

                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(0);
                        }
                    },CommonData.RefreshTime,CommonData.RefreshTime);


                }
            });



            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    saveCustomersInformations();
//                    Toast.makeText(getApplicationContext(), "Saving success!", Toast.LENGTH_LONG).show();
//                    Properties properties = new Properties();
//                    InputStream input = null;
//                    try {
//                        input = new FileInputStream("BLE.properties");//加载Java项目根路径下的配置文件
//                        properties.load(input);// 加载属性文件
//                    } catch (IOException io) {
//                    } finally {
//                        if (input != null) {
//                            try {
//                                input.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    Log.d("properties saved:", properties.getProperty("1") + properties.getProperty("2") + properties.getProperty("3"));

                    Visitors.visitormap.clear();
                    Visitors.clearBleStandard();
                    CommonData.WholePeople = 0;
                    Spinner bleStandard = (Spinner)findViewById(R.id.spinner_ble_standard);
                    String spinnerStr = bleStandard.getSelectedItem().toString();
                    Visitors.setBleStandard(spinnerStr);

                    for (int i = 0; i < 10; i++) {
                        TextView major = (TextView) findViewById(majorList.get(i));
                        String strMajor = major.getText().toString();

                        TextView minor =  (TextView)findViewById(minorList.get(i));
                        String strMinor = minor.getText().toString();

                        EditText name =  (EditText) findViewById(namesList.get(i));
                        String strName = name.getText().toString();

                        Spinner seat =  (Spinner) findViewById(seatsList.get(i));
                        String strSeat = seat.getSelectedItem().toString();

                        Spinner feature = (Spinner)findViewById(featuresList.get(i));
                        String strFeature = feature.getSelectedItem().toString();

                        if ("".equals(strMajor) || "".equals(strMinor) || "".equals(strName) || "".equals(strSeat)){
                            continue;
                        } else {
                            Visitor visitor = new Visitor();
                            visitor.name = strName;
                            visitor.seat = strSeat;
                            visitor.feature = strFeature;

                            CommonData.WholePeople++;

                            if (Visitors.visitormap.containsKey(strMajor)){
                                Visitors.visitormap.get(strMajor).put(strMinor, visitor);
                            } else {
                                HashMap<String, Visitor> myVisitor = new HashMap<>();
                                myVisitor.put(strMinor, visitor);
                                Visitors.visitormap.put(strMajor, myVisitor);
                            }
                        }

                    }

                    Toast.makeText(getApplicationContext(), getText(R.string.save_ok), Toast.LENGTH_LONG).show();
                }
            });

            mResetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    deleteFile("config.properties");
//                    Properties properties = new Properties();
//                    InputStream input = null;
//                    try {
//                        input = new FileInputStream("BLE.properties");//加载Java项目根路径下的配置文件
//                        properties.load(input);// 加载属性文件
//                    } catch (IOException io) {
//                    } finally {
//                        if (input != null) {
//                            try {
//                                input.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    mResetButton.setText(properties.getProperty("3"));

                    for (int i = 0; i < 10; i++) {
                        TextView major =  (TextView)findViewById(majorList.get(i));
                        major.setText("");

                        TextView minor =  (TextView)findViewById(minorList.get(i));
                        minor.setText("");

                        TextView distance =  (TextView)findViewById(distantsList.get(i));
                        distance.setText("");

                        EditText name =  (EditText) findViewById(namesList.get(i));
                        name.setText("");

                        Spinner seat =  (Spinner) findViewById(seatsList.get(i));
                        seat.setSelection(0);

                        Spinner feature = (Spinner)findViewById(featuresList.get(i));
                        feature.setSelection(0);
                    }

                    CommonData.WholePeople = 0;
                    Visitors.visitormap.clear();
                    Visitors.clearBleStandard();
                    Spinner bleStandard = (Spinner)findViewById(R.id.spinner_ble_standard);
                    bleStandard.setSelection(Visitors.getBle_standard());
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

    private String calculateDistance(Double accuracy) {
        StringBuilder sb = new StringBuilder();
        if (accuracy != null) {
            int d = BLE.calculateProximity(accuracy);
            switch (d) {
                case 1:
                    sb.append("圏内");
                    break;
                case 2:
                    sb.append("近隣");
                    break;
//                case 3:
//                    sb.append("遠方");
//                    break;
                case 0:
                    sb.append("圏外");
                    break;
            }
        } else {
            sb.append("圏外");
        }
        return sb.toString();
    }

    private void startScanBLE() {
//        mBluetoothAdapter.startLeScan(scanCallback);
//        mScanning = true;
//        ListView deviceListView = (ListView) findViewById(R.id.listview_settings);
//        mDAdapter = new DAdapter(this, R.layout.list_settings,
//                new ArrayList<ScannedDevice>());
//        try {
//            Thread.sleep(3000);
//            deviceListView.setAdapter(mDAdapter);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private void saveCustomersInformations(){
//        ListView list= (ListView)findViewById(R.id.listview_settings);//获得listview
//        for (int i = 0; i < list.getChildCount(); i++) {
//            LinearLayout layout = (LinearLayout) list.getChildAt(i);// 获得子item的layout
//            EditText tName = (EditText) layout.findViewById(R.id.textview_settings_name);// 从layout中获得控件,根据其id
//            EditText tSeat = (EditText) layout.findViewById(R.id.textview_settings_seat);
//            EditText tAppear = (EditText) layout.findViewById(R.id.textview_settings_appear);
//            mDAdapter.updateCustomers(i, tName.getText().toString(), tSeat.getText().toString(), tAppear.getText().toString());
//        }
//        mDAdapter.updateProperties();
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
