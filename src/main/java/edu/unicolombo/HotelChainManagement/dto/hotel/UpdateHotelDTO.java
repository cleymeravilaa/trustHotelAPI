package edu.unicolombo.HotelChainManagement.dto.hotel;

import edu.unicolombo.HotelChainManagement.domain.model.Hotel;

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
    