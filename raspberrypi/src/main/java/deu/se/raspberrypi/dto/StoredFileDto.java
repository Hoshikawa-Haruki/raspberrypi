/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 파일 업로드 후 저장된 파일명과 전체 경로를 담는 DTO
 * @author Haruki
 */

@Getter
@Setter
public class StoredFileDto {

    private String savedName;  // 서버에 저장된 파일명 (UUID 포함)
    private String fullPath;   // 파일의 실제 저장 경로
}