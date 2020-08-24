package com.example.timify;

public class alarmer {

    String time;
    Boolean[] dayList=new Boolean[7];

    public alarmer(String time, Boolean[] dayList) {
        this.time = time;
        this.dayList = dayList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean[] getDayList() {
        return dayList;
    }

    public void setDayList(Boolean[] dayList) {
        this.dayList = dayList;
    }
}
