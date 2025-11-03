package edu.unicolombo.trustHotelAPI.dto.hotel;

import edu.unicolombo.trustHotelAPI.domain.model.Hotel;

public record RegisterNewHotelDTO(
        String name,
        Integer category,
        String address,
        String phone,
        long floors) {

    public RegisterNewHotelDTO(Hotel hotel){
        this(hotel.getName(), hotel.getCategory(), hotel.getAddress(),hotel.getPhone(), hotel.getFloors());
    }
}
