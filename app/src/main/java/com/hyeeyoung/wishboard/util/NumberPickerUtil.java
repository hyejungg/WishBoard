package com.hyeeyoung.wishboard.util;

import android.widget.NumberPicker;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NumberPickerUtil {
    public static final int TIME_PICKER_INTERVAL = 5; // @param : 5분 단위의 알림 시간 지정
    public NumberPicker type, date, hour, minute; // @param : 알림 유형 및 날짜 넘버피커

    /**
     * @param dates : 팝업창에 디스플레이할 날짜 배열로, 포맷은 MMM dd일 EEE로 설정함
     * @param dates_server : DB에 저장될 날짜 배열로 datetime 타입 포맷인 yyyy-mm-dd hh:mm:ss로 설정함
     */
    public String[] noti_types_array; // @param : 알림 유형
    public String[] dates, dates_server; // @param : 팝업창에 디스플레이할 날짜 배열과 DB에 저장될 날짜 배열
    public Calendar now = Calendar.getInstance();

    public NumberPickerUtil() {
        dates = getDatesFromCalender();
        dates_server = getDatesFromCalenderForServer();
    }

    // @brief : 알림 날짜 넘버피커 초기설정
    public void initNumperPicker() {
        // @brief : 타입 넘버피커 설정
        type.setValue(0);
        type.setMaxValue(noti_types_array.length - 1);
        type.setDisplayedValues(noti_types_array); //@param : 디스플레이될 날짜 값 설정
        type.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // @brief : 날짜 넘버피커 설정
        date.setMinValue(0);
        date.setMaxValue(dates.length - 1);
        date.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return dates[value];
            }
        });
        date.setDisplayedValues(dates); //@param : 디스플레이될 날짜 값 설정
        date.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // @brief : 시간 설정, 0~23시 까지 넘버피커 설정
        hour.setMinValue(0); // @param : 범위의 최소값을 설정
        hour.setMaxValue(23); // @param : 범위의 최댓값을 설정
        hour.setValue((now.get(Calendar.HOUR_OF_DAY)+1) % 24); // @param : 초기 설정값을 현재 시간 + 1로 설정 //@todo : 지정인 경우 날짜 까지 다음날로 바꾸기 및 테스트 해보기
        hour.setWrapSelectorWheel(true); // @param : +/- 버튼디자인에서 휠스크롤 디자인으로 변경
        hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // @param : 키보드로 값을 수정하지 못하도록 포커스 막기

        // @brief :  분 설정, 0~59분 까지 넘버피커 설정
        minute.setMinValue(0);
        minute.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1); // @brief : 인수는 minutes 배열의 인덱스를 의미
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
            minutes.add(String.format("%02d", i)); //@brief : 2자리 수 형식으로 5분 단위 분을 추가
        }
        minute.setDisplayedValues(minutes.toArray(new String[0]));
        minute.setWrapSelectorWheel(true);
        minute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    // @brief : 날짜 넘버피커 내 디스플레이 될 날짜값 배열 반환
    private String[] getDatesFromCalender() {
        Calendar c = Calendar.getInstance();
        List<String> dates = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("MMM dd일 EEE"); // @param : 날짜 포맷 지정, MMM과 EEE는 각각 한자리 값(6월, 수)로 표현하기 위한 포맷임

        // @brief : 현재부터 90일 후까지의 날짜 배열 생성
        dates.add(dateFormat.format(c.getTime())); // @brief : 현재날짜
        for (int i = 0; i < 90; i++) {
            c.add(Calendar.DATE, 1);
            dates.add(dateFormat.format(c.getTime()));
        }

        return dates.toArray(new String[dates.size() - 1]);
    }

    // @brief : 서버에 저장될 날짜값 배열 반환
    private String[] getDatesFromCalenderForServer() {
        Calendar c = Calendar.getInstance();
        List<String> dates_server = new ArrayList<>();
        DateFormat dateFormat_server = new SimpleDateFormat("yyyy-MM-dd"); // @param : 서버에 저장될 날짜 포맷 지정

        // @brief : 현재부터 90일 후까지의 날짜 배열 생성
        dates_server.add(dateFormat_server.format(c.getTime())); // @brief : 현재날짜
        for (int i = 0; i < 90; i++) {
            c.add(Calendar.DATE, 1);
            dates_server.add(dateFormat_server.format(c.getTime()));
        }

        return dates_server.toArray(new String[dates_server.size() - 1]);
    }
}

/**
 * @see : 넘버피커 참고사이트
 * https://stackoverflow.com/questions/35295760/custom-date-time-picker-using-numberpicker-and-fragmentdialog-in-android
 * https://developer.android.com/guide/topics/resources/string-resource?hl=ko#java
 */
