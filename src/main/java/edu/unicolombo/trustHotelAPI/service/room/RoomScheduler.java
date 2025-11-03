package edu.unicolombo.trustHotelAPI.service.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RoomScheduler {

    @Autowired
    private RoomService roomService;

    // Ejecutar todos los dias a las 00:10 AM
    @Scheduled(cron = "0 38 19 * * *")
    public void updateRoomStatusDaily(){
        roomService.updateRoomStatus();
    }
}
