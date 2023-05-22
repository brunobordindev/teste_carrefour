package com.example.testecarrefour.model;

import java.text.SimpleDateFormat;

public class DateUtil {

    public static String dateCurrent(){

        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String yearMonthDateSelect(String date){
        String[] returnDate = date.split("/");
        String month = returnDate[1];
        String year = returnDate[2];

        String monthYear = month + year ;

        return monthYear;
    }
}
