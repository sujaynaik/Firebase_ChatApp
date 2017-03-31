package com.transerve.testfirebase.model;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sujay on 10-03-2017.
 */

public class Chat implements Serializable {

    private String id;
    private int from_id;
    private String from_name;
    private String from_email;
    private String to_device_id;
    private String message;
    //    private String time;
    private Map<String, Object> time;

    public Chat() {
    }

    public Chat(int from_id, String from_name, String from_email, String to_device_id, String message) {
        this.from_id = from_id;
        this.from_name = from_name;
        this.from_email = from_email;
        this.to_device_id = to_device_id;
        this.message = message;

        Map<String, Object> time = new HashMap<>();
        time.put("time", ServerValue.TIMESTAMP);
        Log.e("Chat", "time : " + time);
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getFrom_email() {
        return from_email;
    }

    public void setFrom_email(String from_email) {
        this.from_email = from_email;
    }

    public String getTo_device_id() {
        return to_device_id;
    }

    public void setTo_device_id(String to_device_id) {
        this.to_device_id = to_device_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getTime() {
        return time;
    }

    public void setTime(Map<String, Object> time) {
        this.time = time;
    }

    @Exclude
    public long getTimeLong() {
        return (long) time.get("time");
    }
}
