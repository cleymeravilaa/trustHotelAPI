package edu.unicolombo.trustHotelAPI.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import edu.unicolombo.trustHotelAPI.dto.staying.StayingDTO;
import edu.unicolombo.trustHotelAPI.dto.staying.UpdateStayingDTO;
import edu.unicolombo.trustHotelAPI.service.StayingService;

@RestController
@RequestMapping("/api/v1/stayings")
public class StayingController {

    @Autowired
    public StayingService stayingService;

    @PostMapping("/check-in/{bookingId}")
    public ResponseEntity<StayingDTO> toCheckIn(@PathVariable Long bookingId, UriComponentsBuilder uriBuilder){
        var registeredStaying = stayingService.toCheckIn(bookingId);
        URI url = uriBuilder.path("/stayings/{stayingId}").buildAndExpand(registeredStaying.stayingId()).toUri();
        return ResponseEntity.created(url).body(registeredStaying);
    }

    @GetMapping
    public ResponseEntity<List<StayingDTO>> getAllStayings(){
        return ResponseEntity.ok(stayingService.getAllStayings());
    }

    @GetMapping("/{stayingId}")
    public ResponseEntity<StayingDTO> getStayingById(@PathVariable Long stayingId){
        var staying = stayingService.getStayingById(stayingId);
        return ResponseEntity.ok(staying);
    }

    @PutMapping("/check-out/{stayingId}")
    public ResponseEntity<String> enqueueCheckOut(@PathVariable Long stayingId, @RequestBody UpdateStayingDTO data){
        stayingService.enqueueCheckOut(stayingId, data);
        return ResponseEntity.accepted().body("Check-out en cola para procesamiento");
    }

    @PostMapping("/check-out/undo")
    public ResponseEntity<String> undoLastCheckOut(){
        stayingService.undoLastCheckOut();
        return ResponseEntity.ok("Ultimo check-out revertido");
    }

}
