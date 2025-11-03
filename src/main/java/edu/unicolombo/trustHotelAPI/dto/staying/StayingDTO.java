package edu.unicolombo.trustHotelAPI.dto.staying;

import java.time.LocalDate;
import java.util.List;

import edu.unicolombo.trustHotelAPI.domain.model.Staying;

public record StayingDTO(Long stayingId, LocalDate startDate, LocalDate endDate,
                        Long bookingId, List<StayingRoomDTO> stayingRooms, Long invoiceId) {

    public StayingDTO(Staying staying){
        this(staying.getStayingId(), staying.getStartDate(), 
        staying.getEndDate(), staying.getBooking().getBookingId(), 
        staying.getStayingRoom().stream().map(
                StayingRoomDTO::new
        ).toList(), staying.getInvoice()!=null ? staying.getInvoice().getInvoiceId() : 0);
    }
}
