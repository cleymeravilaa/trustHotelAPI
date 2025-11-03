package edu.unicolombo.trustHotelAPI.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoomStatus {
    FREE("LIBRE"),
    BOOKED("RESERVADA"),
    OCCUPIED("OCUPADA"),
    MAINTENANCE("MANTENIMIENTO");

    private final String displayName;

    RoomStatus(String displayName){
        this.displayName = displayName;
    }

    @JsonCreator
    public static RoomStatus fromString(String value){
        for(RoomStatus status : RoomStatus.values()){
            if (status.name().equalsIgnoreCase(value) ||
                    status.displayName.equalsIgnoreCase(value)){
                return status;
            }
        }
        throw new IllegalArgumentException("Estado de habitacion no valido! "+ value);
    }

    @JsonValue
    public String getDisplayName(){
        return displayName;
    }
}
