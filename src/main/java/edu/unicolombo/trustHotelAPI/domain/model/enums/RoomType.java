package edu.unicolombo.trustHotelAPI.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoomType {
    SINGLE("PERSONAL"),
    DOUBLE("DOBLE"),
    TRIPLE("TRIPLE"),
    SUITE("SUITE"),
    STANDARD("ESTANDAR"),
    FAMILY("FAMILIAR");

    private final String displayName;

    RoomType(String displayName) {
        this.displayName = displayName;
    }

    @JsonCreator
    public static RoomType fromString(String value) {
        for (RoomType type : RoomType.values()) {
            if (type.name().equalsIgnoreCase(value) ||
                    type.displayName.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de habitación no válido: " + value);
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
