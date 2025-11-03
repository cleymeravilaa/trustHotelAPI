package edu.unicolombo.trustHotelAPI.service.room;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import edu.unicolombo.trustHotelAPI.domain.model.Booking;
import edu.unicolombo.trustHotelAPI.domain.model.enums.BookingStatus;
import edu.unicolombo.trustHotelAPI.domain.model.enums.RoomStatus;
import edu.unicolombo.trustHotelAPI.domain.repository.BookingRepository;
import edu.unicolombo.trustHotelAPI.dto.room.FindAvailableRoomsDTO;
import edu.unicolombo.trustHotelAPI.service.room.validations.ValidatorDates;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.unicolombo.trustHotelAPI.domain.model.Room;
import edu.unicolombo.trustHotelAPI.domain.repository.HotelRepository;
import edu.unicolombo.trustHotelAPI.domain.repository.RoomRepository;
import edu.unicolombo.trustHotelAPI.dto.room.RegisterNewRoomDTO;
import edu.unicolombo.trustHotelAPI.dto.room.RoomDTO;
import edu.unicolombo.trustHotelAPI.dto.room.UpdateRoomDTO;

@Service
public class RoomService {

    @Autowired
    public RoomRepository roomRepository;

    @Autowired
    public HotelRepository hotelRepository;

    @Autowired
    public BookingRepository bookingRepository;

    @Autowired
    public List<ValidatorDates> datesValidations;

    public Room registerRoom(RegisterNewRoomDTO data){
        var room = new Room(data);
        System.out.println("precio base: "+ data.basePrice());
        var hotel = hotelRepository.getReferenceById(data.hotelId());
        room.setHotel(hotel);
        hotel.getRooms().add(room);
        return roomRepository.save(room);
    }

    public Room getRoomById(Long id){
        return roomRepository.getReferenceById(id);
    }

    public List<RoomDTO> getAllRooms(){
        updateRoomStatus();
        return roomRepository.findAll()
                    .stream().map(RoomDTO::new).collect(Collectors.toList());
    }

    public void deleteById(Long roomId){
        roomRepository.deleteById(roomId);
    }

    public RoomDTO updateRoom(long roomId, UpdateRoomDTO data){
        Room room = roomRepository.getReferenceById(roomId);
        room.updateData(data);
        return new RoomDTO(roomRepository.save(room));
    }

    @Transactional
    public void updateRoomStatus(){
        LocalDate currentDate = LocalDate.now();
        List<Booking> bookings = bookingRepository.findActiveBookings(currentDate);

        for(Booking booking: bookings){
            List<Room> bookedRooms = booking.getRooms();
            if (booking.getStatus().equals(BookingStatus.PENDING)){
                bookedRooms.forEach(r -> r.setCurrentState(RoomStatus.BOOKED));
            } else if (booking.getStatus().equals(BookingStatus.CANCELED)
                        || booking.getStatus().equals(BookingStatus.COMPLETED)){
                bookedRooms.forEach(r -> r.setCurrentState(RoomStatus.FREE));
            } else if (booking.getStatus().equals(BookingStatus.CONFIRMED)){
                bookedRooms.forEach(r -> r.setCurrentState(RoomStatus.OCCUPIED));
            }

        }
        System.out.println("Estado de las habitaciones actualizados a la fecha " + currentDate);
    }

    public List<Room> findAvailableRooms(FindAvailableRoomsDTO data){
        datesValidations.forEach(val -> val.validateDate(data.startDate(), data.endDate()));
        return roomRepository.findAvailableRooms(data.hotelId(), data.startDate(), data.endDate());
    }

}
