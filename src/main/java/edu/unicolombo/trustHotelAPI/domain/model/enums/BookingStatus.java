package edu.unicolombo.trustHotelAPI.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BookingStatus {
    PENDING("PENDIENTE"),
    CONFIRMED("CONFIRMADA"),
    CANCELED("CANCELADA"),
    COMPLETED("COMPLETADA");

    private final String displayName;

    BookingStatus(String displayName){
        this.displayName = displayName;
    }

    @JsonCreator
    public static BookingStatus fromString(String value){
        for(BookingStatus status : BookingStatus.values()){
            if (status.name().equalsIgnoreCase(value) ||
                    status.displayName.equalsIgnoreCase(value)){
                return status;
            }
        }
        throw new IllegalArgumentException("Estado de reservacion no valido! "+ value);
    }

    @JsonValue
    public String getDisplayName(){
        return displayName;
    }
}
