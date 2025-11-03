package edu.unicolombo.trustHotelAPI.service.room.validations;

import java.time.LocalDate;

public interface ValidatorDates {
    public void validateDate(LocalDate startDate, LocalDate endDate);
}
