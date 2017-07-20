package com.panotech.ble_master_system;

/**
 * Created by sylar on 2017/07/16.
 */

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.panotech.ble_master_system_webconnect.Customer;
import com.panotech.ble_master_system_webconnect.CustomerLab;

import java.util.ArrayList;

/**
 * Created by sylar on 2017/07/16.
 */

public class CustomerListFragment extends ListFragment {
    private ArrayList<Customer> mCustomers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setTitle("FUCK");
        mCustomers = CustomerLab.get(getActivity()).getCustomers();

        CustomerAdapter adapter = new CustomerAdapter(mCustomers);
        setListAdapter(adapter);
    }


    private class CustomerAdapter extends ArrayAdapter<Customer> {
        public CustomerAdapter(ArrayList<Customer> customers) {
            super(getActivity(), 0, customers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.list_customer, null);
            }
            // Configure the view for this Crime
            Customer c = getItem(position);
            /********
             TextView titleTextView = (TextView) convertView
             .findViewById(R.id.crime_list_item_titleTextView);
             titleTextView.setText(c.getTitle());
             TextView dateTextView = (TextView) convertView
             .findViewById(R.id.crime_list_item_dateTextView);
             dateTextView.setText(c.getDate().toString());
             CheckBox solvedCheckBox = (CheckBox) convertView
             .findViewById(R.id.crime_list_item_solvedCheckBox);
             solvedCheckBox.setChecked(c.isSolved());
             *******/

            TextView mNameTextView = (TextView)convertView.findViewById(R.id.list_customer_name);
            mNameTextView.setText(c.getName());
            TextView mSeatTextView = (TextView)convertView.findViewById(R.id.list_seat_number);
            mSeatTextView.setText(c.getSeat());
            TextView mSexTextView = (TextView)convertView.findViewById(R.id.list_customer_sex);
            mSexTextView.setText(c.getSex());
            //TextView mDistanceTextView = (TextView)convertView.findViewById(R.id.list_customer_distance);
            return convertView;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((CustomerAdapter) getListAdapter()).notifyDataSetChanged();
    }

}

