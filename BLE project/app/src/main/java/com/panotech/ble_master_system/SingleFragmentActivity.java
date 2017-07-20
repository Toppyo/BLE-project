package com.panotech.ble_master_system;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by sylar on 2017/07/16.
 */

public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fragment);

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = (CustomerListFragment)fm.findFragmentById(R.id.fragmentContainer);

            if (fragment == null) {
                fragment = createFragment();
                fm.beginTransaction().add(R.id.fragmentContainer, fragment)
                        .commit();
            }
        }
}
