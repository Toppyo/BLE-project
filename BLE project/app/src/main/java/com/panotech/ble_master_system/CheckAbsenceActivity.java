package com.panotech.ble_master_system;

import android.support.v4.app.Fragment;

public class CheckAbsenceActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CustomerListFragment();
    }

}


