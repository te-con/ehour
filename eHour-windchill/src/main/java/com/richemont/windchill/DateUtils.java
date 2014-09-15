package com.richemont.windchill;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author laurent.linck
 */
public class DateUtils {

    private static Logger LOGGER = Logger.getLogger("com.richemont.windchill.DateHelper");


    public static String convertDateToString(Date date, SimpleDateFormat simpleDateFormat) {
        if (simpleDateFormat == null )simpleDateFormat =  new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");  //17-10-12 06:00:00
        StringBuilder newFormatedDate = new StringBuilder( simpleDateFormat.format( date ) );
        return newFormatedDate.toString();
    }


    public static Date convertStringToDate(String dateStr, SimpleDateFormat simpleDateFormat){
        if (simpleDateFormat == null )simpleDateFormat =  new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");  //17-10-12 06:00:00
        //logger.debug("convertStringToDate: " + dateStr);
        Date date = null;
        if (dateStr != null) {
            try {
                date = (Date)simpleDateFormat.parse(dateStr);
            } catch (ParseException e) {
                LOGGER.error("ERROR: Cannot convert String '" + dateStr + "' to Date with format " + simpleDateFormat.toPattern() );
                e.printStackTrace();
            }
        }
        return date;
    }

    public static Date getEndOfCurrentFiscalYear(){
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.setTime(currentDate);
        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.DAY_OF_YEAR,0); //first day of the year.
        calendar.add(Calendar.MONTH,3);
        calendar.add(Calendar.HOUR,0);
        calendar.add(Calendar.MINUTE,0);
        calendar.add(Calendar.SECOND,0);
        return calendar.getTime();
    }

    public static String getEndOfCurrentFiscalYearStr(SimpleDateFormat simpleDateFormat){
        if (simpleDateFormat == null )simpleDateFormat =  new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");  //17-10-12 06:00:00
        return simpleDateFormat.format( getEndOfCurrentFiscalYear() );
    }


    public static DateTime convertJodaTimezone( DateTime date, String srcTz, String destTz) {
        DateTime srcDateTime = date.toDateTime(DateTimeZone.forID(srcTz));
        DateTime dstDateTime = srcDateTime.withZone(DateTimeZone.forID(destTz));
        return dstDateTime.toLocalDateTime().toDateTime();
    }

}
