package edu.unicolombo.trustHotelAPI.service.room.validations;

import edu.unicolombo.trustHotelAPI.infrastructure.errors.exception.BusinessLogicValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class CorrectDateLengthValidation implements ValidatorDates{

    @Override
    public void validateDate(LocalDate startDate, LocalDate endDate) {
        // Validar distancia entre fechas < 24 meses
        LocalDate today = LocalDate.now();
        var monthsDiffUntilStartDate = ChronoUnit.MONTHS.between(today, startDate);
        if (monthsDiffUntilStartDate > 24){
            throw new BusinessLogicValidationException("La distancia entre hoy y la fecha de la reservación excede 24 meses");
        }
    }
}
