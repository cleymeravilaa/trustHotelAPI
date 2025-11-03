package edu.unicolombo.trustHotelAPI.dto.booking;

import java.time.LocalDate;
import java.util.List;

public record UpdateBookingDTO(LocalDate startDate, LocalDate endDate, List<Long> roomIds) {

}
