package com.panotech.ble_master_system;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.panotech.ble_master_system_bluetooth.DeviceAdapter;

import static com.panotech.ble_master_system.TestBLE.REQUEST;
import static com.panotech.ble_master_system_bluetooth.ScannedDevice.asHex;


public class MainActivity extends AppCompatActivity {
    private Button mButtonCheck, mButtonCheckAbsence, mButtonSettings, mButtonBLETest;
    private TextView mTextViewTourName, mTextViewPeopleCounting, mTextViewAppointmentTime;
    private View mMainView;
    private DeviceAdapter mDeviceAdapter;
    public static String CSVFILE;
    private BluetoothAdapter mBluetoothAdapter;

    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, "")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonCheck=(Button)findViewById(R.id.btn_check);
        mButtonCheckAbsence=(Button)findViewById(R.id.btn_check_absence);
        mButtonSettings=(Button)findViewById(R.id.btn_settings);
        mButtonBLETest=(Button)findViewById(R.id.btn_ble_test);

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

        mBluetoothAdapter.startLeScan(scanCallback);


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
                Intent intent4 = new Intent(MainActivity.this, TestBLE.class);
                startActivity(intent4);
            }
        });

        //final Handler handler = new Handler(){
            //@Override
            //public void handleMessage(Message msg){
                //super.handleMessage(msg);
                //if(msg.what == 1){
                    mTextViewTourName = (TextView)findViewById(R.id.tour_input);
                    mTextViewPeopleCounting = (TextView)findViewById(R.id.people_counting);
                    mTextViewAppointmentTime = (TextView)findViewById(R.id.appointment_time);
                    mTextViewTourName.setText("ぶらり京都観光バスの旅:No1");
                    mTextViewPeopleCounting.setText("36人");
                    mTextViewAppointmentTime.setText("8時間");
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
                    String summary = mDeviceAdapter.update(device, rssi, scanRecord);
                    mDeviceAdapter.getList();//addDevice(device);
                    mDeviceAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}
