package com.panotech.ble_master_system;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class CheckAbsenceActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_absence);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = (CustomerListFragment) fm.findFragmentById(R.id.customer_list_container);

        if (fragment == null) {
            fragment = new CustomerListFragment();
            fm.beginTransaction().add(R.id.customer_list_container, fragment)
                    .commit();
        }
    }

}


