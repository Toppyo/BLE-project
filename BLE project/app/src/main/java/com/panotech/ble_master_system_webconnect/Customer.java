package com.panotech.ble_master_system_webconnect;

import java.util.UUID;

/**
 * Created by sylar on 2017/07/14.
 */

public class Customer {
    private String name, seat, appear, distance;
    private int seatNumber;
    private int position;

    public Customer(){
        position = 0;
        name = new String();
        seat = new String();
        appear = new String();
        distance = new String();
        seatNumber = 0;
    }

    public Customer(int iposition, String iname, String iseat, String iappear) {
        position = iposition;
        name = iname;
        seat = iseat;
        appear = iappear;
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

    public String getAppear() {
        return appear;
    }

    public void setAppear(String appear) {
        this.appear = appear;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
