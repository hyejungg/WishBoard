package com.hyeeyoung.wishboard.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

    // @breif : 현제 시간과 알림 시간의 차이
    private static class TIME_MAXIMUM{ // @ brief : 시간 단위 별 최댓값
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int WEEK = 7;
    }

    public static String beforeTime(String str_date){
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = date_format.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        long before = date.getTime();
        long gap = (now - before) / 1000; // @breif : 현제 시간과 알림 시간의 차이

        String msg = ""; // @breif : 알림 아이템에 디스플레이 될 시간 메세지

        // @brief : 시간 차이에 따른 출력 될 알림 메세지 지정
        if (gap < TIME_MAXIMUM.SEC) {
            msg = "방금 전";
        } else if ((gap /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            msg = gap + "분 전";
        } else if ((gap /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            msg = (gap) + "시간 전";
        } else if ((gap /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.WEEK) {
            msg = (gap) + "일 전";
        } else {
            gap /= TIME_MAXIMUM.WEEK;
            msg = (gap) + "주 전";
        }

        return msg;
    }
}
