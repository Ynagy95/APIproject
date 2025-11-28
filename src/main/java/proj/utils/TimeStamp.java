package proj.utils;
import java.util.Date;

import java.text.SimpleDateFormat;

public class TimeStamp {
    private TimeStamp() {
        super();
    }

    public static String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(new Date());
    }


}
