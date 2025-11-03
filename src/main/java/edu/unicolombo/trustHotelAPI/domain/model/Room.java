package edu.unicolombo.trustHotelAPI.domain.model;

import edu.unicolombo.trustHotelAPI.domain.model.enums.RoomStatus;
import edu.unicolombo.trustHotelAPI.domain.model.enums.RoomType;
import edu.unicolombo.trustHotelAPI.dto.room.RegisterNewRoomDTO;
import edu.unicolombo.trustHotelAPI.dto.room.UpdateRoomDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "roomId")
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String number;
    private Double pricePerNight;
    private long floor;
    private long capacity;
    @Enumerated(EnumType.STRING)
    private RoomType type;
    @Enumerated(EnumType.STRING)
    private RoomStatus currentState;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;


    public Room(RegisterNewRoomDTO data) {
        this.type = data.type();
        this.pricePerNight = data.basePrice();
        this.currentState = RoomStatus.FREE;
    }

    public void updateData(UpdateRoomDTO data) {
        if (data.type()!=null) {
            this.type = data.type();
        }

        if (data.basePrice()!=null) {
            this.pricePerNight = data.basePrice();
        }

        if(data.status()!=null){
            this.currentState = data.status();
        }
    }
}
