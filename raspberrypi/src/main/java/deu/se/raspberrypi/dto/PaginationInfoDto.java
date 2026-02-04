/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Haruki
 */
@Getter
@AllArgsConstructor
public class PaginationInfoDto {

    private final int currentPage;
    private final int startPage;
    private final int endPage;
    private final int totalPages;
}
