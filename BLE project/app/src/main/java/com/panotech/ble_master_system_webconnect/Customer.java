package com.panotech.ble_master_system_webconnect;

import java.util.UUID;

/**
 * Created by sylar on 2017/07/14.
 */

public class Customer {
    private String name, seat, sex;
    private UUID mUUID;
    private int seatNumber;

    public Customer(){
        mUUID = UUID.randomUUID();
        name = new String();
        seat = new String();
        sex = new String();
        seatNumber = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public UUID getID() {
        return mUUID;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
