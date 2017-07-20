package com.panotech.ble_master_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private Button mButtonCheck, mButtonCheckAbsence, mButtonSettings, mButtonBLETest;
    private TextView mTextViewTourName, mTextViewPeopleCounting, mTextViewAppointmentTime;
    private View mMainView;

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
                Intent intent3 = new Intent(MainActivity.this, SettingsActivity.class);
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
                    mTextViewTourName.setText("Let's go Kyoto!");
                    mTextViewPeopleCounting.setText("36");
                    mTextViewAppointmentTime.setText("8 hours !");
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
}
