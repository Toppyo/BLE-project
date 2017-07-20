package com.panotech.ble_master_system_webconnect;

/**
 * Created by sylar on 2017/07/20.
 */

public class SeatNumber {
    public static String CheckColumn(int CustomerPosition){
        String seat= new String();
        switch (CustomerPosition%4){
            case 1: seat = "A"; break;
            case 2: seat = "B"; break;
            case 3: seat = "C"; break;
            case 0: seat = "D"; break;
        }
        return seat;
    }

    public static int CheckRow(int CustomerPosition){
        int col, row;
        col = (CustomerPosition-1)%4 + 1;
        row = (CustomerPosition-col)/4 + 1;
        return row;
    }

    public static String CheckSeat(int CustomerPosition){
        StringBuilder sb = new StringBuilder();
        sb.append(CheckColumn(CustomerPosition));
        sb.append("-");
        sb.append(Integer.toString(CheckRow(CustomerPosition)));
        return sb.toString();
    }
}
