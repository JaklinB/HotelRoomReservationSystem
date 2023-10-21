package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        DATE_FORMAT.setLenient(false);
    }

    public static Date parseDate(String dateString) throws ParseException {
        return DATE_FORMAT.parse(dateString);
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static boolean isValidDateRange(Date checkInDate, Date checkOutDate) {
        return checkInDate.before(checkOutDate);
    }

    public static boolean isDateInFuture(Date date) {
        Date today = new Date();
        return date.after(today);
    }
}