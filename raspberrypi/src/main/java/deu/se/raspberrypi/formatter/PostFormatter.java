/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Haruki
 */
public class PostFormatter {

    public static String maskIp(String ip) {
        if (ip == null) {
            return "-";
        }
        String[] parts = ip.split("\\.");
        return parts.length >= 2 ? parts[0] + "." + parts[1] : ip;
    }

    public static String dateformat(LocalDateTime t) {
        if (t == null) {
            return "";
        }
        return t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
