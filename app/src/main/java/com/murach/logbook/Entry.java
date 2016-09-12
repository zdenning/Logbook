package com.murach.logbook;

import java.util.Date;

/**
 * Created by zacdenning on 4/14/16.
 */
public class Entry {
    private String entryID;
    private String glucose;
    private String carbs;
    public String time;

    public Entry() {

    }

    public Entry(String entryID, String glucose, String time) {
        this.glucose = glucose;
        this.time = time;
    }

    public Entry (String entryID, String glucose, String carbs, String time) {
        this.glucose = glucose;
        this.carbs = carbs;
        this.time = time;
    }


    public String getEntryID() { return entryID; }

    public void setEntryID() {this.entryID = entryID;}

    public String getGlucose() { return glucose; }

    public void setGlucose(String glucose) { this.glucose = glucose; }

    public String getCarbs() { return carbs; }

    public void setCarbs(String carbs) { this.carbs = carbs; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }



}
