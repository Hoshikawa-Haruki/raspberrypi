/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.se.raspberrypi.idempotency;

/**
 * 중복 요청(idempotencyKey)을 판단하기 위한 저장소 인터페이스.
 * 같은 key로 여러 번 호출되더라도 최초 1회만 true를 반환하도록 구현.
 * 
 * @author Haruki
 */
public interface IdempotencyStore {

    /**
     * 주어진 key에 대해 처리 권한을 획득 시도한다.
     *
     * @param key 아이뎀포턴시 키 (요청을 구분하는 값)
     * @return true : 최초 요청 (처리 가능) false : 중복 요청 (이미 처리됨)
     */
    boolean tryAcquire(String key);
}
