package com.shamsaha.app.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToDateConversion {

    private Date date;
    private String dateOut;
    private DateFormat dateFormat;
    private SimpleDateFormat format;

    public String dateConversion(String inputDate, String inputFormat) throws ParseException {

        format = new SimpleDateFormat(inputFormat);
        date = format.parse(inputDate);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateOut = dateFormat.format(date);
        return dateOut;
    }
}
