package com.mercadolibre.orbit.app.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

public abstract class DateUtil {

    public static int getDaysDifference(LocalDate di, LocalDate df) {
        return Math.round(DAYS.between(di, df));
    }

    public static LocalDate dateToLocalDate(Date date) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return localDateTime.toLocalDate();
    }


    public static Date sumDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }


}
