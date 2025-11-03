package edu.unicolombo.trustHotelAPI.service.room.validations;

import edu.unicolombo.trustHotelAPI.infrastructure.errors.exception.BusinessLogicValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class CorrectPeriodDaysValidation implements ValidatorDates{
    @Override
    public void validateDate(LocalDate startDate, LocalDate endDate) {
        var nightsByDateRange = ChronoUnit.DAYS.between(startDate, endDate);
        if (nightsByDateRange < 1  || nightsByDateRange > 30){
            throw new BusinessLogicValidationException("No se permite esta cantidad de noches en la reservacion");
        }
    }
}
