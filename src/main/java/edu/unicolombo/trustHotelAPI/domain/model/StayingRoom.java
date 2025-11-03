package edu.unicolombo.trustHotelAPI.domain.model;

import java.time.LocalDate;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "staying_room")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StayingRoom {

    @EmbeddedId
    private StayingRoomId id;
    // Mapear parte la clave compuesta
    @ManyToOne
    @MapsId("stayingId")
    @JoinColumn(name = "staying_id")
    private Staying staying;
    // Mapear la otra parte de la clave
    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name  = "room_id")
    private Room room;

    // Otros atributos importantes 
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String notes;
}
