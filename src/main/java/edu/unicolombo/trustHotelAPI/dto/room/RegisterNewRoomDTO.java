package edu.unicolombo.trustHotelAPI.dto.room;

import edu.unicolombo.trustHotelAPI.domain.model.enums.RoomType;
import edu.unicolombo.trustHotelAPI.domain.model.Room;


public record RegisterNewRoomDTO(long hotelId,  RoomType type, Double basePrice) {

    public RegisterNewRoomDTO(Room room){
        this(room.getHotel().getHotelId(), room.getType(), room.getPricePerNight());
    }
}
