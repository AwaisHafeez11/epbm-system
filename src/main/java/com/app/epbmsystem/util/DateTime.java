package com.app.epbmsystem.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime {
    /**
     * This function is giving us date and time in a defined format
     * @return
     * @throws ParseException
     */
    public static Date getDateTime() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String date = formatter.format(cal.getTime());
        Date currentTime = formatter.parse(date);
        return currentTime;
    }

    /**
     * Fetch only sql date
     * @return
     * @throws ParseException
     */
    public static java.sql.Date getSqlDate() {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        System.out.println("utilDate:" + utilDate);
        System.out.println("sqlDate:" + sqlDate);
        return sqlDate;
    }

    /**
     * This method is adding 10 minutes in the current time which we are using to setup expire time of a token
     * @return
     * @throws ParseException
     */
    public static Date getExpireTime() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10);
        String date = formatter.format(cal.getTime());
        Date expireTime = formatter.parse(date);
        return expireTime;
    }
}
