package com.richemont.jira;

import com.richemont.windchill.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author laurent.linck
 */
public class Utilities {


    public static String formatDate(DateTime dateTime) {
        String JIRA_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        DateTimeFormatter JIRA_DATE_TIME_FORMATTER = DateTimeFormat.forPattern(JIRA_DATE_TIME_PATTERN);
        DateTimeFormatter JIRA_DATE_FORMATTER = ISODateTimeFormat.date();
        return JIRA_DATE_FORMATTER.print(dateTime);
    }

    public static DateTime parseDateTimeOrDate(String str) {
        String JIRA_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        DateTimeFormatter JIRA_DATE_TIME_FORMATTER = DateTimeFormat.forPattern(JIRA_DATE_TIME_PATTERN);
        DateTimeFormatter JIRA_DATE_FORMATTER = ISODateTimeFormat.date();
        try {
            return JIRA_DATE_TIME_FORMATTER.parseDateTime(str);
            } catch (Exception ignored) {
                return JIRA_DATE_FORMATTER.parseDateTime(str);

        }
    }


    /**
     * input date: 2013-05-07T21:59:59.999Z
     * output date 2013-04-07T21:59:59.999+0530
     * @param date
     * @return
     */
    public static String formatToWorklogJiraDate(Date date){
        //DateTime dt = new DateTime(date);
        //String test = ISODateTimeFormat.dateTime().withZone(DateTimeZone.forTimeZone(TimeZone.getDefault())).print(new DateTime(date));
        //System.out.println("datime test=" + test);
        String dateTimeEndTimesheetEntryStr = new DateTime(date).toString("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");  // 2013-05-07T23:59:59.999+02:00

        // TBD
        // heure d'ete
        dateTimeEndTimesheetEntryStr = dateTimeEndTimesheetEntryStr.replace("+02:00","+0200") ;  // 2013-04-30T10:30:18.932+0530
        // heure d'hiver
        dateTimeEndTimesheetEntryStr = dateTimeEndTimesheetEntryStr.replace("+01:00","+0100") ;  // 2013-04-30T10:30:18.932+0530

        return dateTimeEndTimesheetEntryStr;
    }

    /**
     *
     * @param hm
     * @return
     */
    public static StringBuilder getHashMapContentForDisplay(HashMap<String, Object> hm) {
        StringBuilder sb = new StringBuilder("");
        if  (hm != null ) {
            for( Iterator it = hm.keySet().iterator(); it.hasNext();) {
                String theKey = (String)it.next();
                String theValue = "null";
                String theClass = "null";
                if (hm.get(theKey)!= null) theClass = hm.get(theKey).getClass().toString();
                if (hm.get(theKey) instanceof  String) theValue= (String)hm.get(theKey);
                else if (hm.get(theKey) instanceof Date) theValue= DateUtils.convertDateToString((Date) hm.get(theKey), JiraConst.SIMPLE_DATE_FORMAT);
                else theValue = "?";
                sb.append("\t").append(theKey).append(" = ").append(theValue).append(" [").append(theClass).append("]").append("\n");
            }
        }
        return sb;
    }

    protected static boolean isNumber(String str) {
        if (str == null) return false;
        return str.matches("[-+]?[0-9]*[,\\.]?[0-9]+$");
    }

    protected static boolean isNaN(String str) {
        if (str == null) return true;
        return !str.matches("[-+]?[0-9]*[,\\.]?[0-9]+$");
    }

}
