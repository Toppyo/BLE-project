package com.panotech.ble_master_system_webconnect;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by sylar on 2017/07/14.
 */

public class CustomerLab {
    private static CustomerLab sCustomerLab;
    private ArrayList<Customer> mCustomers;
    private Context mContext;

    //DEMO part**************************
    private String randomSeat(int i){
        StringBuilder sb = new StringBuilder();
        switch (i%4){
            case 1: sb.append("A");break;
            case 2: sb.append("B");break;
            case 3: sb.append("C");break;
            case 0: sb.append("D");break;
        }
        return sb.toString();
    }

    private String randomSex(int i){
        StringBuilder sb = new StringBuilder();
        switch (i%4){
            case 1: sb.append("woman");break;
            case 2: sb.append("man");break;
            case 3: sb.append("Owoman");break;
            case 0: sb.append("Oman");break;
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
            customer.setName("Ctm #" + (i+1));
            customer.setSeat(randomSeat(i+1));
            customer.setSex(randomSex(i+1));
            mCustomers.add(customer);         //fuck you!!!!!!!
        }

    }

    public ArrayList<Customer> getCustomers() {
        return mCustomers;
    }

    public Customer getCustomer(UUID id){
        for(Customer customer: mCustomers){
            if(customer.getID().equals(id)){
                return customer;
            }
        }
        return null;
    }
}
