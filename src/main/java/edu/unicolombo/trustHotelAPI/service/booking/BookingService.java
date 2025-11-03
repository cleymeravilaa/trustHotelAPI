package edu.unicolombo.trustHotelAPI.service.booking;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import edu.unicolombo.trustHotelAPI.infrastructure.errors.exception.BusinessLogicValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.unicolombo.trustHotelAPI.domain.model.Booking;
import edu.unicolombo.trustHotelAPI.domain.model.Room;
import edu.unicolombo.trustHotelAPI.domain.model.enums.RoomStatus;
import edu.unicolombo.trustHotelAPI.domain.repository.BookingRepository;
import edu.unicolombo.trustHotelAPI.domain.repository.CustomerRepository;
import edu.unicolombo.trustHotelAPI.domain.repository.HotelRepository;
import edu.unicolombo.trustHotelAPI.domain.repository.RoomRepository;
import edu.unicolombo.trustHotelAPI.dto.booking.BookingDTO;
import edu.unicolombo.trustHotelAPI.dto.booking.RegisterBookingDTO;
import edu.unicolombo.trustHotelAPI.dto.booking.UpdateBookingDTO;
import jakarta.transaction.Transactional;

@Service
public class BookingService {

    @Autowired
    public BookingRepository bookingRepository;

    @Autowired
    public RoomRepository roomRepository;

    @Autowired 
    public CustomerRepository customerRepository;

    @Autowired
    public HotelRepository hotelRepository;

    @Transactional
    public BookingDTO registerBooking(RegisterBookingDTO data) throws BusinessLogicValidationException{

        var customer = customerRepository.getReferenceById(data.customerId());
        var hotel = hotelRepository.getReferenceById(data.hotelId());
        var rooms = roomRepository.findByHotel_hotelIdAndRoomIdIn(hotel.getHotelId(), data.roomIds());

        var advanceDeposit = reserveRoomsAndCalculeAdvanceDeposit(data.startDate(), data.endDate(), rooms);
        var booking = new Booking(customer, hotel, rooms, data.startDate(), data.endDate(), advanceDeposit);
        roomRepository.saveAll(rooms);
        return new BookingDTO(bookingRepository.save(booking));
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(BookingDTO::new).toList();
    }

    public BookingDTO getBookingById(Long bookingId) {
        return new BookingDTO(bookingRepository.getReferenceById(bookingId));
    }

    public void deleteById(Long bookingId) {

        Booking booking = bookingRepository.getReferenceById(bookingId);
        List<Room> rooms = booking.getRooms();
        for(Room room: rooms){
            room.setCurrentState(RoomStatus.FREE);
        }
        roomRepository.saveAll(rooms);
       bookingRepository.deleteById(bookingId); 
    }

    public BookingDTO updateBooking(Long bookingId, UpdateBookingDTO data) {
        var booking = bookingRepository.getReferenceById(bookingId);

        if (data.startDate()!=null) {
            booking.setStartDate(data.startDate()); 
        }
        if (data.endDate()!=null) {
            booking.setEndDate(data.endDate());
        }

        if (data.roomIds() !=null  && data.startDate() != null || data.endDate() != null) {
            List<Room> rooms = roomRepository.findByHotel_hotelIdAndRoomIdIn(booking.getHotel().getHotelId(), data.roomIds());
            var advanceDeposit = reserveRoomsAndCalculeAdvanceDeposit(booking.getStartDate(), booking.getEndDate(), rooms);
            booking.setRooms(rooms);
            booking.setAdvanceDeposit(advanceDeposit);
        } else {
            throw new BusinessLogicValidationException("No se puede realizar la actualizacion de la reserva!");
        }
        return new BookingDTO(bookingRepository.save(booking));
    }

    public Double reserveRoomsAndCalculeAdvanceDeposit(LocalDate startDate, LocalDate endDate, List<Room> rooms){
        // calcular el deposito por adelantado de las reservaciones de las habitaciones
        Double total = 0D;
        Long bookingDays = ChronoUnit.DAYS.between(startDate, endDate);

        for(Room room: rooms){
            room.setCurrentState(RoomStatus.BOOKED);
            total += bookingDays * room.getPricePerNight();
        }
        return total * .2;
    }

    public List<BookingDTO> getBookingByCustomerDni(String customerDni) {
        return bookingRepository.findBookingByCustomer(customerDni).stream().map(BookingDTO::new).toList();
    }
}
