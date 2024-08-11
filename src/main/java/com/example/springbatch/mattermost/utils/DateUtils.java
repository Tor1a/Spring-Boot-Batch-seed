package com.example.springbatch.mattermost.utils;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String getCurrentWeekOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // calendar에서의 월은 0부터 시작
        int day = calendar.get(Calendar.DATE);

        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);

        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

        // 첫 주에 해당하지 않는 주의 경우 전 달 마지막 주차로 계산
        if (weekOfMonth == 0) {
            calendar.add(Calendar.DATE, -day); // 전 달의 마지막 날 기준
            return getCurrentWeekOfMonth(calendar.getTime());
        }

        // 마지막 주차의 경우
        if (weekOfMonth == calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE)); // 이번 달의 마지막 날
            int lastDaysDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 이번 달 마지막 날의 요일

            // 마지막 날이 월~수 사이이면 다음달 1주차로 계산
            if (lastDaysDayOfWeek >= Calendar.MONDAY && lastDaysDayOfWeek <= Calendar.WEDNESDAY) {
                calendar.add(Calendar.DATE, 1); // 마지막 날 + 1일 => 다음달 1일
                return getCurrentWeekOfMonth(calendar.getTime());
            }
        }

        return year + "년 " + month + "월 " + weekOfMonth + "주차";
    }
}
