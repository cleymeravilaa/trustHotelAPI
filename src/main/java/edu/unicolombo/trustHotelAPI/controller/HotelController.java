package edu.unicolombo.trustHotelAPI.controller;

import edu.unicolombo.trustHotelAPI.domain.repository.HotelRepository;
import edu.unicolombo.trustHotelAPI.dto.hotel.HotelDTO;
import edu.unicolombo.trustHotelAPI.dto.hotel.RegisterNewHotelDTO;
import edu.unicolombo.trustHotelAPI.dto.hotel.UpdateHotelDTO;
import edu.unicolombo.trustHotelAPI.service.HotelService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController {

    @Autowired
    public HotelRepository hotelRepository;
    @Autowired
    public HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDTO> registerHotel(@RequestBody RegisterNewHotelDTO data, UriComponentsBuilder uriBuilder){
        var registeredHotel = hotelService.register(data);
        URI url = uriBuilder.path("/hotels/{hotelId}").buildAndExpand(registeredHotel.hotelId()).toUri();
        return ResponseEntity.created(url).body(registeredHotel);
    }

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable long hotelId) {
        return ResponseEntity.ok(hotelService.getHotelById(hotelId));
    }

    @DeleteMapping("/{hotelId}")
    @Transactional
    public ResponseEntity<Void> deleteHotel(@PathVariable long hotelId) {
        hotelService.deleteById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{hotelId}")
    @Transactional
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable long hotelId, @RequestBody UpdateHotelDTO data) {
        return ResponseEntity.ok(hotelService.updateHotel(hotelId, data));
    }

}
