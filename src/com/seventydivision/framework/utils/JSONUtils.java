package com.seventydivision.framework.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by andreaascari on 28/01/14.
 */
public class JSONUtils {

    public static final String DEFAULT_NULL_DATE_FORMATTED = "0000-00-00 00:00:00";

    public static Date parseDate(String date) {
        try {
            if (date == null) {
                date = DEFAULT_NULL_DATE_FORMATTED;
            }
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String parseDateWithJoda(String date) {
        DateTimeFormatter dfm = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = dfm.parseDateTime(date);
        return dt.toString("dd/MM/yyyy");
    }

    public static String parseTimeWithJoda(String date) {
        DateTimeFormatter dfm = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = dfm.parseDateTime(date);
        return dt.toString("HH:mm:ss");
    }

    public static String parseTimeNoSecsWithJoda(String date) {
        DateTimeFormatter dfm = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = dfm.parseDateTime(date);
        return dt.toString("HH:mm");
    }

    public static DateTime parseDateTimeWithJoda(String date) {
        DateTimeFormatter dfm = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = dfm.parseDateTime(date);
        return dt;
    }

    public static String getLocalDateTimeFormatted() {
        Calendar calendar = Calendar.getInstance();
        DateTime currentLocalTime = new DateTime(calendar.getTime());
        DateTimeFormatter dfm = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return currentLocalTime.toString(dfm);
    }

    public static boolean isValidDate(String date) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNullDate(String date) {
        return date.equals(DEFAULT_NULL_DATE_FORMATTED);
    }

}