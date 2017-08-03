package com.panotech.ble_master_system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.panotech.ble_master_system_bluetooth.BLE;
import com.panotech.ble_master_system_bluetooth.CommonData;
import com.panotech.ble_master_system_bluetooth.DeviceAdapter;
import com.panotech.ble_master_system_bluetooth.PickupAdapter;
import com.panotech.ble_master_system_bluetooth.ScannedDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.panotech.ble_master_system_bluetooth.CommonData.WholePeople;

public class CheckAbsenceActivity extends Activity {
    private ListView mListView;
    private TextView mWholeTextView, mPresentTextView, mAbsentTextView;
    private ArrayList<ScannedDevice> mDevices;
    private PickupAdapter mPickupAdapter;
    private Handler mHandler;
    private int PeopleCount = 0;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_absence);
        mListView = (ListView) findViewById(R.id.pickup_listview);
        mWholeTextView = (TextView)findViewById(R.id.pickup_whole_people);
        mPresentTextView = (TextView)findViewById(R.id.pickup_present_people);
        mAbsentTextView = (TextView)findViewById(R.id.pickup_absent_people);
        mDevices = updateList(CommonData.mDeviceAdapter.getList());
        mPickupAdapter = new PickupAdapter(this, R.layout.list_customer,
                new ArrayList<ScannedDevice>());
        PeopleCount = mPickupAdapter.updatePickup(mDevices);
//        mPickupAdapter.notifyDataSetChanged();
        mListView.setAdapter(mPickupAdapter);
        initCheckUI();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                mPickupAdapter.clear();
                mDevices = updateList(CommonData.mDeviceAdapter.getList());
                PeopleCount = mPickupAdapter.updatePickup(mDevices);
//                mPickupAdapter.notifyDataSetChanged();
                mListView.setAdapter(mPickupAdapter);
                initCheckUI();
            }
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        },CommonData.RefreshTime,CommonData.RefreshTime);
    }
    private void initCheckUI(){
        int PresentPeople = WholePeople - PeopleCount;
        mWholeTextView.setText(String.valueOf(WholePeople));
        mPresentTextView.setText(String.valueOf(PresentPeople));
        mAbsentTextView.setText(String.valueOf(PeopleCount));
    }

    private ArrayList<ScannedDevice> updateList(List<ScannedDevice> list){
        ArrayList<ScannedDevice> devices = new ArrayList<ScannedDevice>();
        int pos = 0;
        while (pos < list.size()){
            ScannedDevice device = list.get(pos++);
            if(Visitors.visitormap.containsKey(Integer.toString(device.getBLE().getMajor()))
                    && Visitors.visitormap.get(Integer.toString(device.getBLE().getMajor())).containsKey(Integer.toString(device.getBLE().getMinor()))
                    && BLE.calculateProximity(device.getAveAccuracy()) != BLE.PROXIMITY_NEAR) {
                devices.add(device);
            }
        }
        return devices;
    }
}


