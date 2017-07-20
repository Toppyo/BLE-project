package com.panotech.ble_master_system_webconnect;

import java.util.UUID;

/**
 * Created by sylar on 2017/07/12.
 */

public class Message {
    private String mUpdateStringTourName, mUpdateStringAppointmentTime;
    private int  mUpdateStringPeopleCounting;
    private UUID mID;
    private boolean mUpdate;

    public Message(){
        mID = UUID.randomUUID();
        mUpdateStringTourName = new String();
        mUpdateStringPeopleCounting = 0;
        mUpdateStringAppointmentTime = new String();
        mUpdate = true;
    }

    public UUID getID() {
        return mID;
    }

    public String getUpdateStringTourName() {
        return mUpdateStringTourName;
    }

    public void setUpdateStringTourName(String updateStringTourName) {
        mUpdateStringTourName = updateStringTourName;
    }

    public int getUpdateStringPeopleCounting() {
        return mUpdateStringPeopleCounting;
    }

    public void setUpdateStringPeopleCounting(int updateStringPeopleCounting) {
        mUpdateStringPeopleCounting = updateStringPeopleCounting;
    }

    public String getUpdateStringAppointmentTime() {
        return mUpdateStringAppointmentTime;
    }

    public void setUpdateStringAppointmentTime(String updateStringAppointmentTime) {
        mUpdateStringAppointmentTime = updateStringAppointmentTime;
    }
}
