package edu.ewubd.cse489120251;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    private static Util instance = new Util();
    private Util(){}
    public static Util getInstance(){
        return instance;
    }
    /////////////////////////////////////////////
    public String convertMillisecondsToDate(long milliseconds, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(milliseconds);
        return sdf.format(date);
    }
    public long convertDateToMilliseconds(String dateString, String dateFormat) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Date date = formatter.parse(dateString);
        return date.getTime();
    }
}
