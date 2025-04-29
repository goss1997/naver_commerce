package com.naver.commerce.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter ISO8601_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final DateTimeFormatter MINUTE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


    private DateTimeUtil() {
        // 생성자 private으로 막아서 util 클래스로만 사용
    }

    public static String formatToIso8601(OffsetDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return ISO8601_FORMATTER.format(dateTime);
    }

    public static String formatToMinute(OffsetDateTime dateTime) {
        return dateTime.format(MINUTE_FORMATTER);
    }

    public static String formatToMinuteKST(OffsetDateTime dateTime) {
        return dateTime.atZoneSameInstant(ZoneOffset.ofHours(9)) // 한국 시간대(+09:00)로 변환
                .format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"));
    }
}
