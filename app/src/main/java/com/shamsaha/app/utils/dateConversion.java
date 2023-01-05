package com.shamsaha.app.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class dateConversion {

    private Date date;
    private String dateOut;
    private DateFormat dateFormat;
    private SimpleDateFormat format;

    public String dateConversion(String inputDate, String inputFormat) throws ParseException {

        format = new SimpleDateFormat(inputFormat);
        date = format.parse(inputDate);
        dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        dateOut = dateFormat.format(date);
        return dateOut;
    }
}
