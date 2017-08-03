package com.panotech.ble_master_system;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panotech.ble_master_system_bluetooth.CommonData;
import com.panotech.ble_master_system_bluetooth.ScannedDevice;
import com.panotech.ble_master_system_utils.LimitedSizeQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sylar on 2017/07/30.
 */

public class TestShowLogActivity extends Activity {
    private ListView mListView;
    private Button mRefreshButton;
    private myAdapter mAdapter;
    private LimitedSizeQueue<String> mStrings = new LimitedSizeQueue<>(299);
    private List<ScannedDevice> mDevices;
    private Handler mHandler;
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlog);
        mListView = (ListView) findViewById(R.id.log_listview);
        mRefreshButton = (Button) findViewById(R.id.log_btn_refresh);
//        mHandler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                mDevices = CommonData.mDeviceAdapter.getList();
//                while (pos < mDevices.size()){
//                    ScannedDevice device = mDevices.get(pos++);
//                    if(device.getBLE() != null) {
//                        CommonData.TestLogQueue.add("******" + device.getBLE().toString() +
//                                "  AveDistance:" + String.valueOf(device.getAveAccuracy()) + "  " + device.timeToString());
//                        Log.i("LOGavedistance", device.getBLE().toString() +
//                                "  AveDistance:" + String.valueOf(device.getAveAccuracy()) + "  " + device.timeToString());
//                    }
//                }
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                mHandler.sendEmptyMessage(0);
//            }
//        },1000,1000);
        mStrings = CommonData.TestLogQueue;
        mAdapter = new myAdapter(getApplicationContext(), R.layout.test_list_logdata, mStrings);
        mAdapter.notifyDataSetChanged();
        mListView.setAdapter(mAdapter);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(
                        getApplicationContext(), "Refreshing success!", Toast.LENGTH_SHORT
                ).show();
                mStrings = CommonData.TestLogQueue;
                mAdapter = new myAdapter(getApplicationContext(), R.layout.test_list_logdata, mStrings);
                mAdapter.notifyDataSetChanged();
                mListView.setAdapter(mAdapter);
            }
        });
    }

    private class myAdapter extends ArrayAdapter<String> {
        private LimitedSizeQueue<String> mList;
        private LayoutInflater mInflater;
        private int mResId;
        private TextView mDataTextView;

        public myAdapter(Context context, int resID, LimitedSizeQueue<String> list) {
            super(context, resID, list);
            mResId = resID;
            mList = list;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String logData = mList.get(mList.size() - position - 1);
            if (convertView == null) {
                convertView = mInflater.inflate(mResId, null);
            }
            mDataTextView = (TextView) convertView.findViewById(R.id.list_logdata);
            mDataTextView.setText("No." + String.valueOf(position + 1) + "    " + logData);
            return convertView;
        }
    }
}
