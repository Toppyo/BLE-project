package com.panotech.ble_master_system;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.panotech.ble_master_system_webconnect.Customer;
import com.panotech.ble_master_system_webconnect.CustomerLab;

import java.util.ArrayList;

import static com.panotech.ble_master_system_webconnect.SeatNumber.CheckSeat;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CheckActivity extends Activity {
    private TestTableView mTestTableView;
    private ArrayList<Customer> mCustomers;
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        mCustomers = CustomerLab.get(this).getCustomers();
        mainLayout = (LinearLayout)findViewById(R.id.test_layout_container);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        mTestTableView = new TestTableView(getApplicationContext(), 4);
        TestTableView.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        mTestTableView.setLayoutParams(lp);
        mTestTableView.setStretchAllColumns(true);
        mTestTableView.setPadding(5, 10, 5 , 10);
        AddCustomersRows(mTestTableView, mCustomers);
        seatPainting(mTestTableView, mCustomers);
        mainLayout.addView(mTestTableView);
    }
/*
    private void addCustomersRows(TestTableView view, ArrayList<Customer> customers){
        int rowNumber = 0;
        int position = 0;
        int column = view.getM_ColumnN();
        while(40-column*rowNumber > 0){
            view.AddRow(new String[]{customers.get(position++).getName(), customers.get(position++).getName(), customers.get(position++).getName(), customers.get(position++).getName()});
            rowNumber++;
        }
    }
*/
    private void AddCustomersRows(TestTableView view, ArrayList<Customer> customers){
        int rowNumber = 0;
        int position = 0;
        int CustomerPosition = 1;
        int column = view.getM_ColumnN();
        while(40-column*rowNumber > 0){
            view.addRow(
                    new String[]{CheckSeat(CustomerPosition++), CheckSeat(CustomerPosition++), CheckSeat(CustomerPosition++), CheckSeat(CustomerPosition++)},
                    new String[]{customers.get(position++).getName(), customers.get(position++).getName(), customers.get(position++).getName(), customers.get(position++).getName()});
            rowNumber++;
        }
    }

    private void seatPainting(TestTableView view, ArrayList<Customer> customers){
        for(int i=0; i<40; i++){
            Customer customer = customers.get(i);
            int column = i%4;
            int row = (i - column) / 4;
            if(column<2){
                View v = view.GetCellView(row, column);
                if(i%5 == 0){
                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundred));
                }
                else{
                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundblue));
                }
            }
            else{
                View v = view.GetCellView(row, column+1);
                if(i%7 == 0){
                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundred));
                }
                else{
                    v.setBackground(getDrawable(R.drawable.rectanglebackgroundblue));
                }
            }
        }
    }

}