/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.util;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.rmi.UnknownHostException;

/**
 * 클라이언트의 실제 IP 주소를 추출하는 유틸리티 클래스.
 *
 * @author Haruki
 */
public class IpUtils {

    public static String getClientIp(HttpServletRequest request) {
        // 1. Nginx, Cloudflare 등에서 가장 흔히 사용하는 헤더
        String ip = request.getHeader("X-Forwarded-For");

        // 2. 일부 프록시 서버나 WebLogic, WebSphere 등에서 사용하는 헤더
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        // 3. WebLogic / WebSphere 환경에서 종종 사용되는 헤더
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        // 4. 특정 WAS 환경에서 IP를 전달할 때 사용되는 헤더
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        // 5. 또 다른 프록시 환경에서 사용되는 대체 헤더
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        // 6. 위의 모든 헤더가 없을 경우 (프록시 미사용 시)
        // 실제 요청을 보낸 클라이언트와의 TCP 연결 IP
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 7. 여러 프록시를 거친 경우 "IP1, IP2, IP3" 형식으로 들어올 수 있으므로
        // 가장 앞에 있는 첫 번째 IP가 실제 클라이언트 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }
}
