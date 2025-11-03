package edu.unicolombo.trustHotelAPI.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unicolombo.trustHotelAPI.domain.model.StayingRoom;
import edu.unicolombo.trustHotelAPI.domain.model.StayingRoomId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StayingRoomRepository extends JpaRepository<StayingRoom, StayingRoomId> {

    @Query("SELECT COUNT(sr) > 0 FROM StayingRoom sr WHERE sr.id.stayingId = :stayingId AND sr.id.roomId = :roomId")
    boolean existsByStayingIdAndRoomId(@Param("stayingId") Long stayingId, @Param("roomId") Long roomId);
}
