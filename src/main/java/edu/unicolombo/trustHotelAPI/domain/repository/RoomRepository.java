package edu.unicolombo.trustHotelAPI.domain.repository;

import edu.unicolombo.trustHotelAPI.domain.model.Room;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotel_hotelIdAndRoomIdIn(Long hotelId, List<Long> ids);

    @Query("""
        SELECT r FROM Room r
        WHERE r.hotel.id = :hotelId
          AND r.id NOT IN (
              SELECT br.id FROM Booking b
              JOIN b.rooms br
              WHERE (
                  (:startDate BETWEEN b.startDate AND b.endDate) OR
                  (:endDate BETWEEN b.startDate AND b.endDate) OR
                  (b.startDate BETWEEN :startDate AND :endDate) OR
                  (b.endDate BETWEEN :startDate AND :endDate)
              )
          )
    """)
    List<Room> findAvailableRooms(
            @Param("hotelId") Long hotelId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
