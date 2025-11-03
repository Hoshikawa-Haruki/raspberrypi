/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.mapper;

import deu.se.raspberrypi.dto.PostDto;
import deu.se.raspberrypi.dto.PostListDto;
import deu.se.raspberrypi.dto.StoredFileDto;
import deu.se.raspberrypi.entity.Attachment;
import deu.se.raspberrypi.entity.Post;

/**
 * Mapper는 순수 변환만 담당해야 함
 * 1) SRP 위반 (단일 책임 원칙 위반)
 * 2) Mapper는 DB를 모르고 있어야 함
 * 
 * 2025.11.03.
 * @author Haruki
 */
public class PostMapper {

    // CREATE — DTO → Entity
    public static Post toPostEntity(PostDto dto) {
        Post post = new Post();
        post.setIpAddress(dto.getIpAddress());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        return post;
    }

    // [2] Entity → PostDto (단건 조회용)
    public static PostDto toPostDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setIpAddress(post.getIpAddress());
        dto.setAuthorNameSnapshot(post.getAuthorNameSnapshot());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());

        if (post.getAttachments() != null && !post.getAttachments().isEmpty()) {
            dto.setAttachments(
                    post.getAttachments().stream()
                            .map(PostMapper::toStoredFileDto)
                            .toList()
            );
        }
        return dto;
    }

    // [3] Entity → PostListDto (목록)
    public static PostListDto toPostListDto(Post post) {
        PostListDto dto = new PostListDto();
        dto.setId(post.getId());
        dto.setAuthorNameSnapshot(post.getAuthorNameSnapshot());
        dto.setTitle(post.getTitle());
        dto.setIpAddress(post.getIpAddress());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }

    // [공용] Attachment → StoredFileDto
    private static StoredFileDto toStoredFileDto(Attachment attachment) {
        StoredFileDto f = new StoredFileDto();
        f.setId(attachment.getId());
        f.setUuid(attachment.getUuid());
        f.setExt(attachment.getExt());
        f.setOriginalName(attachment.getOriginalName());
        return f;
    }
}
