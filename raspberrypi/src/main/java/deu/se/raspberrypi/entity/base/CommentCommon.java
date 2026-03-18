/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity.base;

import deu.se.raspberrypi.entity.Member;
import java.time.LocalDateTime;

/**
 *
 * @author Haruki
 */
public interface CommentCommon {

    Long getId();
    String getAuthorNameSnapshot();
    String getContent();
    Member getMember();
    LocalDateTime getCreatedAt();
}
