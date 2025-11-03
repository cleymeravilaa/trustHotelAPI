package edu.unicolombo.trustHotelAPI.dto.hotel;

import edu.unicolombo.trustHotelAPI.domain.model.Hotel;

public record UpdateHotelDTO(
        String name,
        Integer category,
        String address,
        String phone) {

    public UpdateHotelDTO(Hotel hotel){
        this(
                hotel.getName(),
                hotel.getCategory(),
                hotel.getAddress(),
                hotel.getPhone()
            );
    }
}
    