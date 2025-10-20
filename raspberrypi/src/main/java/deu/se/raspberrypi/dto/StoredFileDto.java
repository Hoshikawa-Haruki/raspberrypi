/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 파일 업로드 후 저장된 파일명과 전체 경로를 담는 DTO
 *
 * 사용 클래스 위치
 * 1. FileService: 업로드 완료 후 반환용 객체
 * 2. BoardPostService: FileService로부터 받은 정보를 Attachment 엔티티로 변환 및 DB 저장
 * 3. BoardPostDto: 조회 시 첨부파일 정보 전달용 필드 (List<StoredFileDto> attachments)
 *
 * 👉 StoredFileDto는 업로드 → DB 저장 → 게시글 조회까지 파일 정보를 주고받는 매개체 역할을 담당함.
 *
 * @author Haruki
 */
@Getter
@Setter
public class StoredFileDto {

    private String originalName; // 원본 파일명 (사용자에게 보여줄 용도)
    private String uuid; // uuid
    private String ext; // 확장자
}