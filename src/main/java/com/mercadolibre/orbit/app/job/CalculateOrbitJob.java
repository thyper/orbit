package com.mercadolibre.orbit.app.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



@Component
public class CalculateOrbitJob {

    @Scheduled(cron = "0 0 12 * * *")
    public void calculateOrbitStatus() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
    }

}
