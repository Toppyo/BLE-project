package com.panotech.ble_master_system_utils;

import com.panotech.ble_master_system_bluetooth.ScannedDevice;

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by sylar on 2017/07/23.
 */

public class PropertyUtil {
    public static final int UUIDNUM   = 1;
    public static final int MAJORNUM  = 2;
    public static final int MINORNUM  = 3;
    public static final int NAMEMUM   = 4;
    public static final int SEATNUM   = 5;
    public static final int APPEARNUM = 6;
    public static final int CUSTMOERNUM = 40;

    public void writeProperties(int i, int major, int minor, String name, String seat, String appear) {
        Properties properties = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("config.properties");
            properties.setProperty(Integer.toString(i*MAJORNUM),  Integer.toString(major));
            properties.setProperty(Integer.toString(i*MINORNUM),  Integer.toString(minor));
            properties.setProperty(Integer.toString(i*NAMEMUM),   name);
            properties.setProperty(Integer.toString(i*SEATNUM),   seat);
            properties.setProperty(Integer.toString(i*APPEARNUM), appear);//保存键值对到内存
            properties.store(output, "Date: " + new Date().toString());// 保存键值对到文件中
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");//加载Java项目根路径下的配置文件
            properties.load(input);// 加载属性文件
        } catch (IOException io) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int checkProperties(int major, int minor){
        Properties prop = new Properties();
        InputStream input = null;
        int position = 0;
        try {
            String filename = "config.properties";
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return 0;
            }
            prop.load(input);

            Set<Object> keys = prop.keySet();//返回属性key的集合
            for(Object key:keys){
                System.out.println("key:"+key.toString()+",value:"+prop.get(key));
            }
            for(int i=0; i<CUSTMOERNUM; i++){
                if(prop.getProperty(Integer.toString(i*MAJORNUM)).equals(major) && prop.getProperty(Integer.toString(i*MINORNUM)).equals(minor)){
                    position = i+1;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return position;
        }
    }

}
