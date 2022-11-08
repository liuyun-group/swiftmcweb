package com.liuyun.swiftmcweb.core.util.time;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Time utilities.
 *
 * @author Flyan
 * @version 1.0
 * @date 2022/2/9 10:53
 */
@Slf4j
public class TimeUtils {

    public static final long SECOND_MILLIS  = 1000;
    public static final long MINUTE_MILLIS  = 60 * SECOND_MILLIS;
    public static final long HOUR_MILLIS    = 60 * MINUTE_MILLIS;
    public static final long DAY_MILLIS     = 24 * HOUR_MILLIS;
    public static final long MONTH_MILLIS   = 30 * DAY_MILLIS;
    public static final long YEAR_MILLIS    = 365 * DAY_MILLIS;

    /**
     * Get yesterday date time.
     */
    public static Date yesterday() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static boolean validateDateStringFormat(String dateString, String format) {
        try {
            stringToDate(dateString, format);
            return true;
        } catch (ParseException ignored) {  }
        return false;
    }

    /**
     * Date to string.
     */
    public static
    String dateToString(Date date, String formatString) {
        return new SimpleDateFormat(formatString).format(date);
    }

    /**
     * String to date.
     */
    public static
    Date stringToDate(String dateString, String formatString) throws ParseException {
        return new SimpleDateFormat(formatString).parse(dateString);
    }

    /**
     * Convert a date string from one format to another.
     * Eg.  "2000-08-08" -> "2000.08.08"
     */
    public static
    String forFormatDateString(String dateString, String srcFormatString, String destFormatString) {
        if(dateString == null || dateString.isEmpty()
                || srcFormatString == null || srcFormatString.isEmpty()
                || destFormatString == null || destFormatString.isEmpty()) {
            return "";  /* Don't show. */
        }

        DateFormat srcFormat = new SimpleDateFormat(srcFormatString);
        DateFormat destFormat = new SimpleDateFormat(destFormatString);
        try {
            return destFormat.format(srcFormat.parse(dateString));
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * Get the number of days that are now away from the target date
     *
     * @param date
     * @return
     */
    public static int durationDays(Date date) {
        if(date == null) {
            return 0;
        }

        return durationDays(date.getTime());
    }

    /**
     * Get the number of days that are now away from the target timestamp
     *
     * @param timeMs
     * @return
     */
    public static int durationDays(long timeMs) {
        long nowMs;
        try {
            nowMs = stringToDate(dateToString(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd").getTime();
        } catch (ParseException e) {
            nowMs = System.currentTimeMillis();
        }

        long duration = nowMs - timeMs;
        int days = (int) (duration / DAY_MILLIS);
        boolean carry = duration % DAY_MILLIS > 0;

        return carry ? days + 1 : days;
    }

    /**
     *
     *
     * @return
     */
    public static int getNowWeek(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Just now time...
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    /**
     * Just now time but need formatting date.
     *
     * @param formatString format, default has yyyy, MM, dd, HH, mm, ss
     * @return
     */
    public static Date now(String formatString) {
        if(formatString != null) {
            try {
                return stringToDate(dateToString(new Date(), formatString), formatString);
            } catch (ParseException e) {
                log.warn("TimeUtil::stringToDate format string error, format: {}", formatString);
            }
        }

        return new Date();
    }

}
