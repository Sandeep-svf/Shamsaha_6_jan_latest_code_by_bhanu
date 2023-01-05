package com.shamsaha.app.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class String2DateConversion {

    private Date date;
    private String dateOut;
    private DateFormat dateFormat;
    private SimpleDateFormat format;

    public Date String2DateConversion(String inputDate, String inputFormat) throws ParseException {

        format = new SimpleDateFormat(inputFormat);
        date = format.parse(inputDate);
        return date;
    }
}
