package com.mycompany.onelinememo;

/**
 * Created by LG on 2015-07-13.
 */
public class MyItem {
    String Memo;
    String Date;
    String Time;
    int Color;
    MyItem(String aaMemo, String aaDate, String aaTime, int aaColor) {
        Memo = aaMemo;
        Date = aaDate;
        Time = aaTime;
        Color = aaColor;
    }
    String getMemo() {
        return Memo;
    }
}