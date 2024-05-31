package Adaptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AdaptorImpl implements Adaptor{
    @Override
    public long translatorStringToLong(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    @Override
    public String translatorLongToString(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        return dateFormat.format(date);
    }
}
