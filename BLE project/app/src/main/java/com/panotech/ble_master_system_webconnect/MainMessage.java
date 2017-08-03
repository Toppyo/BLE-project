package com.panotech.ble_master_system_webconnect;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sylar on 2017/07/12.
 */

public class MainMessage {
    private static MainMessage sMainMessage;
    private List<Message> mMessages;

    public static MainMessage get(Context context){
        if(sMainMessage == null){
            sMainMessage = new MainMessage(context);
        }
        return sMainMessage;
    }

    private MainMessage(Context context){
        mMessages = new ArrayList<>();
        Message message = new Message();
        message.setUpdateStringTourName("Let's go Kyoto!");
        message.setUpdateStringPeopleCounting(36);
        message.setUpdateStringAppointmentTime("8 hours");
    }

    public List<Message> getMessages(){
        return mMessages;
    }

    public Message getMessage(UUID id){
        for(Message message: mMessages){
            if(message.getID().equals(id)){
                return message;
            }
        }
        return null;
    }
}
