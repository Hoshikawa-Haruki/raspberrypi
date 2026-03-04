/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.raspberrypi.entity;

import deu.se.raspberrypi.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 중복요청방지 TTL 기록용 엔티티
 *
 * @author Haruki
 */
@Entity
@Table(
        name = "idempotency_key",
        uniqueConstraints = @UniqueConstraint(columnNames = "idemKey")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdempotencyKey extends BaseEntity {

    @Column(nullable = false, length = 128)
    private String idemKey;

    public IdempotencyKey(String idemKey) {
        this.idemKey = idemKey;
    }
}
