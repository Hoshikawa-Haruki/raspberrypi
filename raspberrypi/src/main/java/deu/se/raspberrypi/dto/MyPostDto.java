/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 마이페이지 내 작성글 표시용 DTO
 *
 * 2026.01.14.
 *
 * @author Haruki
 */
@Getter
@Setter
public class MyPostDto {

    private Long id;
    private String title;
    private String formattedCreatedAt;
}
