package com.panotech.ble_master_system_webconnect;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static com.panotech.ble_master_system_webconnect.SeatNumber.CheckSeat;

/**
 * Created by sylar on 2017/07/14.
 */

public class CustomerLab {
    private static CustomerLab sCustomerLab;
    private ArrayList<Customer> mCustomers;
    private Context mContext;

    //DEMO part**************************

    private String randomName(){
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        int a = r.nextInt(8);
        switch (a%8){
            case 0: sb.append("乗員若男"); break;
            case 1: sb.append("乗員若女"); break;
            case 2: sb.append("乗員老男"); break;
            case 3: sb.append("乗員老女"); break;
            case 4: sb.append("乗員中男"); break;
            case 5: sb.append("乗員中女"); break;
            case 6: sb.append("乗員子男"); break;
            case 7: sb.append("乗員子女"); break;
        }
        return sb.toString();
    }
    private String randomSex(String string){
        StringBuilder sb = new StringBuilder();
        if(string.equals("乗員若男")) sb.append("MY");
        if(string.equals("乗員若女")) sb.append("FY");
        if(string.equals("乗員老男")) sb.append("MS");
        if(string.equals("乗員老女")) sb.append("FS");
        if(string.equals("乗員中男")) sb.append("MM");
        if(string.equals("乗員中女")) sb.append("FM");
        if(string.equals("乗員子男")) sb.append("MC");
        if(string.equals("乗員子女")) sb.append("FC");
        return sb.toString();
    }

    private String randomDistance(){
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        int a = r.nextInt(4);
        switch (a%4){
            case 0: sb.append("圏外"); break;
            case 1: sb.append("圏内"); break;
            case 2: sb.append("遠方"); break;
            case 3: sb.append("近隣"); break;
        }
        return sb.toString();
    }
    //*******************

    public static CustomerLab get(Context context){
        if(sCustomerLab == null){
            sCustomerLab = new CustomerLab(context);
        }
        return sCustomerLab;
    }

    private CustomerLab(Context context){
        mContext = context;
        mCustomers = new ArrayList<>();
        //just for DEMO *************:need to be changed:**************

        for(int i=0; i<50; i++){
            Customer customer = new Customer();
            customer.setSeatNumber(i+1);
            customer.setName(randomName());
            customer.setSeat(CheckSeat(i+1));
            customer.setAppear(randomSex(customer.getName()));
            customer.setDistance(randomDistance());
            mCustomers.add(customer);         //fuck you!!!!!!!
        }

    }

    public ArrayList<Customer> getCustomers() {
        return mCustomers;
    }

    public Customer getCustomer(int position){
        for(Customer customer: mCustomers){
            if(customer.getPosition() == position){
                return customer;
            }
        }
        return null;
    }
}
