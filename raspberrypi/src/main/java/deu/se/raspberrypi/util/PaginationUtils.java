/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.util;

import deu.se.raspberrypi.dto.PaginationInfoDto;
import org.springframework.data.domain.Page;

/**
 *
 * @author Haruki
 */
public final class PaginationUtils {

    private PaginationUtils() {
    }

    public static PaginationInfoDto of(Page<?> page, int blockSize) {
        int currentPage = page.getNumber();
        int totalPages = page.getTotalPages();

        int startPage = 0;
        int endPage = -1;

        if (totalPages > 0) {
            startPage = (currentPage / blockSize) * blockSize;
            endPage = Math.min(startPage + blockSize - 1, totalPages - 1);
        }

        return new PaginationInfoDto(
                currentPage,
                startPage,
                endPage,
                totalPages
        );
    }
}
