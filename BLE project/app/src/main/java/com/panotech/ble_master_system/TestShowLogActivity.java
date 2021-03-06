package com.panotech.ble_master_system;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.panotech.ble_master_system_bluetooth.CommonData;
import com.panotech.ble_master_system_utils.LimitedSizeQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sylar on 2017/07/30.
 */

public class TestShowLogActivity extends Activity {
    private ListView mListView;
    private Button mRefreshButton;
    private myAdapter mAdapter;
    private List<String> mStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlog);
        mListView = (ListView) findViewById(R.id.log_listview);
        mRefreshButton = (Button) findViewById(R.id.log_btn_refresh);
        try {
            mStrings = update(CommonData.TestLogQueue);
            mAdapter = new myAdapter(getApplicationContext(), R.layout.test_list_logdata, mStrings);
            mAdapter.notifyDataSetChanged();
            mListView.setAdapter(mAdapter);
            mRefreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(
                            getApplicationContext(), "Refreshing success!", Toast.LENGTH_SHORT
                    ).show();
                    mStrings = update(CommonData.TestLogQueue);
                    mAdapter = new myAdapter(getApplicationContext(), R.layout.test_list_logdata, mStrings);
                    mAdapter.notifyDataSetChanged();
                    mListView.setAdapter(mAdapter);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private class myAdapter extends ArrayAdapter<String> {
        private List<String> mList;
        private LayoutInflater mInflater;
        private int mResId;
        private TextView mDataTextView;

        public myAdapter(Context context, int resID, List<String> list) {
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

    public List update(LimitedSizeQueue rawData){
        List<String> newList = new ArrayList<>();
        newList.addAll(rawData);
        return newList;
    }
}
