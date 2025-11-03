package edu.unicolombo.trustHotelAPI.dto.booking;

import java.time.LocalDate;
import java.util.List;

import edu.unicolombo.trustHotelAPI.domain.model.Booking;
import edu.unicolombo.trustHotelAPI.domain.model.enums.BookingStatus;
import edu.unicolombo.trustHotelAPI.dto.room.RoomDTO;

public record BookingDTO(Long bookingId, String customerDni, long hotelId, String customerName, String hotelName, List<RoomDTO> rooms,
                         LocalDate startDate, LocalDate endDate, Double advanceDeposit, BookingStatus status) {

    public BookingDTO(Booking booking){
        this(booking.getBookingId(), booking.getCustomer().getDni(),
                booking.getHotel().getHotelId(),
                booking.getCustomer().getName(),
                booking.getHotel().getName(),
                booking.getRooms().stream().map(RoomDTO::new).toList(), booking.getStartDate()
                , booking.getEndDate(), booking.getAdvanceDeposit(), booking.getStatus());
    }

}
