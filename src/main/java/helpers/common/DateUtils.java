package helpers.common;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class DateUtils {

    public static final String PATTERN_REPORTER = "MM/dd/yyyy HH:mm:ss";

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat(PATTERN_REPORTER);
        return dateFormat.format(new Date());
    }
}
