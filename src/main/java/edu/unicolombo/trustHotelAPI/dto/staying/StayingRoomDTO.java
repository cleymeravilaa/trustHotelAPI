package edu.unicolombo.trustHotelAPI.dto.staying;

import java.time.LocalDate;

import edu.unicolombo.trustHotelAPI.domain.model.StayingRoom;

public record StayingRoomDTO(Long roomId, LocalDate checkInDate, LocalDate checkOuDate, String notes) {

    public StayingRoomDTO(StayingRoom stayingRoom){
        this(stayingRoom.getRoom()
                .getRoomId(), stayingRoom.getCheckInDate(), stayingRoom.getCheckOutDate()
                , stayingRoom.getNotes());
    }
}
