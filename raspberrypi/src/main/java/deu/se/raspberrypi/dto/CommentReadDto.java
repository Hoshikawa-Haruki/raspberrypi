/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 조회용 DTO
 * 
 * @author Haruki
 */
@Getter
@Setter
public class CommentReadDto {

    Long id;
    String authorNameSnapshot;
    String content;
    boolean mine;
    String formattedCreatedAt;
}
