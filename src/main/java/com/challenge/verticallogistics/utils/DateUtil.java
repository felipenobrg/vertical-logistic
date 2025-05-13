package com.challenge.verticallogistics.utils;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter LEGACY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateUtil() {
    }

    public static LocalDate parseFromLegacyFormat(String dateString) {
        return LocalDate.parse(dateString, LEGACY_DATE_FORMAT);
    }

    public static String formatToApiFormat(LocalDate date) {
        return date.format(API_DATE_FORMAT);
    }
}