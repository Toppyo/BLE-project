package com.panotech.ble_master_system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.panotech.ble_master_system_bluetooth.BLE;
import com.panotech.ble_master_system_bluetooth.CommonData;
import com.panotech.ble_master_system_bluetooth.DeviceAdapter;
import com.panotech.ble_master_system_bluetooth.ScannedDevice;
import com.panotech.ble_master_system_webconnect.Customer;
import com.panotech.ble_master_system_webconnect.CustomerLab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.panotech.ble_master_system_bluetooth.CommonData.WholePeople;
import static com.panotech.ble_master_system_webconnect.SeatNumber.CheckSeat4;
import static com.panotech.ble_master_system_webconnect.SeatNumber.CheckSeat2;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CheckActivity extends Activity {
    private TestTableView mTestTableView;
    private TextView mWholeTextView, mPresentTextView, mAbsentTextView;
    private LinearLayout mainLayout;
    private List<ScannedDevice> list;
    private int PeopleCount = 0;
    public static int COL = 2;
    public int ROW = 5;
    private HashMap seatMap = new HashMap();
    private Handler mHandler = new Handler();

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        mWholeTextView = (TextView)findViewById(R.id.check_whole_people);
        mPresentTextView = (TextView)findViewById(R.id.check_present_people);
        mAbsentTextView = (TextView)findViewById(R.id.check_absent_people);
        initCheckUI();
        list = CommonData.mDeviceAdapter.getList();
        mainLayout = (LinearLayout)findViewById(R.id.test_layout_container);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        mTestTableView = new TestTableView(getApplicationContext(), COL);
        TestTableView.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        mTestTableView.setLayoutParams(lp);
        mTestTableView.setColumnStretchable(0,true);
        mTestTableView.setColumnStretchable(2,true);
        mTestTableView.setPadding(5, 10, 5 , 10);
        AddCustomersRows(mTestTableView);
        initUI(mTestTableView);
        seatPainting(mTestTableView);
        initCheckUI();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                PeopleCount = 0;
                seatPainting(mTestTableView);
                initCheckUI();
                Log.i("CHECKupdate", "UPDATED!");
            }
        };
        mainLayout.addView(mTestTableView);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        },CommonData.RefreshTime,CommonData.RefreshTime);
    }

    private void AddCustomersRows(TestTableView view) {
        int rowNumber = 0;
        int CustomerPosition = 1;
        int column = view.getM_ColumnN();

        //make a new HashMap for checking-name-by-seat from visitormap
        Iterator iter1 = Visitors.visitormap.entrySet().iterator();
        while (iter1.hasNext()) {
            HashMap.Entry entry1 = (HashMap.Entry) iter1.next();
            HashMap val1 = (HashMap)entry1.getValue();
            Iterator iter2 = val1.entrySet().iterator();
            while (iter2.hasNext()){
                HashMap.Entry entry2 = (HashMap.Entry) iter2.next();
                Visitor visitor = (Visitor)entry2.getValue();
                String st1 = visitor.seat;
                String st2 = visitor.name;
                seatMap.put(st1, st2);
            }
        }
        //
        while (COL * ROW - column * rowNumber > 0) {
            String name1 = new String();
            String name2 = new String();
            String seat1 = CheckSeat2(CustomerPosition++);
            if(seatMap.containsKey(seat1)){
                name1 = (String) seatMap.get(seat1);
            }
            else{
                name1 = "                         ";
            }
            String seat2 = CheckSeat2(CustomerPosition++);
            if(seatMap.containsKey(seat2)){
                name2 = (String) seatMap.get(seat2);
            }
            else{
                name2 = "                         ";
            }
            view.addRow(
                    new String[]{seat1, seat2},
                    new String[]{name1, name2} );
            rowNumber++;
        }
    }

//    private void AddCustomersRows(TestTableView view, ArrayList<Customer> customers){
//        int rowNumber = 0;
//        int position = 0;
//        int CustomerPosition = 1;
//        int column = view.getM_ColumnN();
//        while(COL*ROW-column*rowNumber > 0){
//            view.addRow(
//                    new String[]{CheckSeat2(CustomerPosition++), CheckSeat2(CustomerPosition++)},//change needed..........
//                    new String[]{customers.get(position++).getName(), customers.get(position++).getName()});
//            rowNumber++;
//        }
//    }

    private void seatPainting(TestTableView view){
        int check;//0-- no customer; 1-- cant-be-checked customer; 2-- checked customer
        int position = 0;
        //initial operation to paintMap: Check which seat has Customer
        HashMap seatPaintMap = new HashMap();
        check = 0;
        for(int i=0; i< COL*ROW; i++){
            seatPaintMap.put(i+1, check);
        }
        check = 1;
        Iterator iter3 = seatMap.entrySet().iterator();
        while (iter3.hasNext()){
            HashMap.Entry entry3 = (HashMap.Entry) iter3.next();
            String seat = (String) entry3.getKey();
            int num = seatToNum(seat);
            seatPaintMap.put(num, check);
        }

        while (position < list.size()) {
            ScannedDevice device = list.get(position++);
            int intMajor = device.getBLE().getMajor();
            int intMinor = device.getBLE().getMinor();
            String stMajor = Integer.toString(intMajor);
            String stMinor = Integer.toString(intMinor);
            Log.i("PRINTBEFORE", Integer.toString(device.getBLE().getMajor()) +"  "+ Integer.toString(device.getBLE().getMinor()));
            if (Visitors.visitormap.containsKey(stMajor) && Visitors.visitormap.get(stMajor).containsKey(stMinor)) {
                    String stringSeat = (String) Visitors.visitormap.get(stMajor).get(stMinor).seat;
                    int intSeat = seatToNum(stringSeat);
                    double doubleAveAccuracy = device.getAveAccuracy();
                    int intAveAccuracy = BLE.calculateProximity(doubleAveAccuracy);
                    if(intAveAccuracy == BLE.PROXIMITY_NEAR){
                        seatPaintMap.put(intSeat, 2);
                    }
            }
        }
        //Actual paintWork.
        for(int i=0; i<COL*ROW; i++){
            int checked = (int) seatPaintMap.get(i+1);
            int halfCOL = COL/2;
            int column = i%COL;
            int row = (i - column) / COL;
            if(column<halfCOL){
                View v = view.GetCellView(row, column);
                if(checked == 1){
                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundred));
                }
                else if(checked == 2){
                    PeopleCount++;
                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundblue));
                }
            }
            else{
                View v = view.GetCellView(row, column+1);
                if(checked == 1){
                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundred));
                }
                else if(checked == 2){
                    PeopleCount++;
                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundblue));
                }
            }
        }
    }

//    private void seatPainting(TestTableView view, ArrayList<Customer> customers){
//        for(int i=0; i<COL*ROW; i++){
//            Customer customer = customers.get(i);
//            int halfCOL = COL/2;
//            int column = i%COL;
//            int row = (i - column) / COL;
//            if(column<halfCOL){
//                View v = view.GetCellView(row, column);
//                if(i%5 == 0){
//                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundred));
//                }
//                else{
//                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundblue));
//                }
//            }
//            else{
//                View v = view.GetCellView(row, column+1);
//                if(i%7 == 0){
//                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundred));
//                }
//                else{
//                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundblue));
//                }
//            }
//        }
//    }

    private void initCheckUI(){
//        if(Visitors.settingsMap.containsKey("summary")) {
//            String stringPresentPeople = Visitors.settingsMap.get("summary");
//            mPresentTextView.setText(stringPresentPeople);
//            int intPresentPeople = Integer.parseInt(stringPresentPeople);
//            int intAbsentPeople = intWholePeople - intPresentPeople;
//            mAbsentTextView.setText(String.valueOf(intAbsentPeople));
//        }
        int AbsentPeople = WholePeople - PeopleCount;
        mWholeTextView.setText(String.valueOf(WholePeople));
        mPresentTextView.setText(String.valueOf(PeopleCount));
        mAbsentTextView.setText(String.valueOf(AbsentPeople));
    }

    private void initUI(TestTableView view){
        for(int i=0; i<COL*ROW; i++){
            int halfCOL = COL/2;
            int column = i%COL;
            int row = (i - column) / COL;
            if(column<halfCOL){
                View v = view.GetCellView(row, column);
                v.setBackground(getDrawable(R.drawable.rectanglebackgroundwhite));
            }
            else{
                View v = view.GetCellView(row, column+1);
                v.setBackground(getDrawable(R.drawable.rectanglebackgroundwhite));
            }
        }
    }

    private int seatToNum(String seat){
        int num = 0;
        switch (seat){
            case "A-1": num=1; break;
            case "B-1": num=2; break;
            case "A-2": num=3; break;
            case "B-2": num=4; break;
            case "A-3": num=5; break;
            case "B-3": num=6; break;
            case "A-4": num=7; break;
            case "B-4": num=8; break;
            case "A-5": num=9; break;
            case "B-5": num=10; break;
        }
        return num;
    }
}