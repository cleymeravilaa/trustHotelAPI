package edu.unicolombo.trustHotelAPI.dto.room;

import java.time.LocalDate;

public record FindAvailableRoomsDTO(long hotelId, LocalDate startDate, LocalDate endDate) {
}
