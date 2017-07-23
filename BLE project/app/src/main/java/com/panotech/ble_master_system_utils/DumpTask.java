package com.panotech.ble_master_system_utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.panotech.ble_master_system.R;
import com.panotech.ble_master_system_bluetooth.ScannedDevice;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by sylar on 2017/07/17.
 */

public class DumpTask extends AsyncTask<List<ScannedDevice>, Integer, String> {
    private WeakReference<Context> mRef;

    public DumpTask(WeakReference<Context> ref) {
        mRef = ref;
    }

    @Override
    protected String doInBackground(List<ScannedDevice>... params) {
        if ((params == null) || (params[0] == null)) {
            return null;
        }

        List<ScannedDevice> list = params[0];
        String csvpath = CsvDumpUtil.dump(list);

        return csvpath;
    }

    @Override
    protected void onPostExecute(String result) {
        Context context = mRef.get();
        if (context != null) {
            if (result == null) {
                Toast.makeText(context, R.string.dump_failed, Toast.LENGTH_SHORT).show();
            } else {
                String suffix = context.getString(R.string.dump_succeeded_suffix);
                Toast.makeText(context, result + suffix, Toast.LENGTH_SHORT).show();
            }
        }
    }
}