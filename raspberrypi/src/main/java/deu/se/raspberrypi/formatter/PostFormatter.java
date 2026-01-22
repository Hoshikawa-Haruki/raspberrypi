/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.formatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Haruki
 */
public class PostFormatter {

    private static final DateTimeFormatter DATE
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter TIME
            = DateTimeFormatter.ofPattern("HH:mm");

    private static final DateTimeFormatter DATETIME
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String maskIp(String ip) {
        if (ip == null) {
            return "-";
        }
        String[] parts = ip.split("\\.");
        return parts.length >= 2 ? parts[0] + "." + parts[1] : ip;
    }

    public static String dateFormat(LocalDateTime t) {
        if (t == null) {
            return "";
        }
        return t.format(DATETIME);
    }

    /**
     * 게시글 목록 표시용 날짜 포맷 - 오늘 작성된 글: HH:mm - 오늘 이전 글: yyyy-MM-dd
     *
     * @param t 게시글 작성 시각
     * @return 목록 화면에 표시할 날짜 문자열
     */
    public static String postListDateFormat(LocalDateTime t) {
        if (t == null) {
            return "";
        }

        LocalDate today = LocalDate.now();
        LocalDate createdDate = t.toLocalDate();

        if (createdDate.isEqual(today)) {
            return t.format(TIME);
        }

        return t.format(DATE);
    }
}
