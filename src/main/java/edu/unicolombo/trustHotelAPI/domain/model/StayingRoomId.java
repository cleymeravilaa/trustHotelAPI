package edu.unicolombo.trustHotelAPI.domain.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class StayingRoomId implements Serializable {
    private Long stayingId;
    private Long roomId;

    public StayingRoomId(){}
    public StayingRoomId(Long stayingId, Long roomId){
        this.stayingId = stayingId;
        this.roomId  = roomId;
    }
}