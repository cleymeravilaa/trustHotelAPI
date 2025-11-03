package edu.unicolombo.trustHotelAPI.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.unicolombo.trustHotelAPI.domain.model.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "bookingId")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double advanceDeposit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    // Relacion ManyToMany con habitación (tabla intermedia automatica)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "booking_room", 
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<Room> rooms = new ArrayList<>();

    public Booking(Customer customer, Hotel hotel, List<Room> rooms, 
                    LocalDate startDate, LocalDate endDate, 
                    Double advanceDeposit){
        this.customer = customer;
        this.hotel = hotel;
        this.rooms = rooms;
        this.startDate = startDate;
        this.endDate = endDate;
        this.advanceDeposit = advanceDeposit;
        this.status = BookingStatus.PENDING;
    }
}
