package com.kx.promote.utils;

import java.util.Calendar;
import java.util.Date;

public class DateToolkit {
    public static Date getZeroOfDay(Date date) {
        if(date==null)
            date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date endTime = calendar.getTime();
        return endTime;
    }
    public static Date getTomorrow() {
        Date today = DateToolkit.getZeroOfDay(new Date());
        Date tomorrow = new Date(today.getTime()+1000*60*60*24);
        return tomorrow;
    }
}