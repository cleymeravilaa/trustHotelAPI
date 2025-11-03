package edu.unicolombo.trustHotelAPI.dto.room;

import edu.unicolombo.trustHotelAPI.domain.model.Room;
import edu.unicolombo.trustHotelAPI.domain.model.enums.RoomType;
import edu.unicolombo.trustHotelAPI.domain.model.enums.RoomStatus;


public record RoomDTO(Long roomId, Long hotelId, RoomType type, Double basePrice, RoomStatus status) {

    public RoomDTO(Room room){
        this(room.getRoomId(), room.getHotel().getHotelId(), room.getType(), room.getPricePerNight(), room.getCurrentState());
    }
}
