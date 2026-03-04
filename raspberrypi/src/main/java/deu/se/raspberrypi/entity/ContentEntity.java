/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity;

import java.util.List;

/**
 *
 * @author Haruki
 */
public interface ContentEntity {

    void addAttachment(Attachment attachment);

    void removeAttachment(Attachment attachment);

    List<Attachment> getAttachments();

    void setContent(String content);
}
