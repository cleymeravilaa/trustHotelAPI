package edu.unicolombo.trustHotelAPI.dto.hotel;

import edu.unicolombo.trustHotelAPI.domain.model.Hotel;

public record HotelDTO(long hotelId, String name, int category, String address, String phone, Long floors) {

    public HotelDTO(Hotel hotel){
        this(hotel.getHotelId(), hotel.getName(), hotel.getCategory(), hotel.getAddress(),
                hotel.getPhone(), hotel.getFloors());
    }
}
