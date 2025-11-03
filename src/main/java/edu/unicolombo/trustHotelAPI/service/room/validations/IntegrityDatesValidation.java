package edu.unicolombo.trustHotelAPI.service.room.validations;

import edu.unicolombo.trustHotelAPI.infrastructure.errors.exception.BusinessLogicValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class IntegrityDatesValidation implements  ValidatorDates{

    @Override
    public void validateDate(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)){
            throw new BusinessLogicValidationException("Las fechas no son validas");
        }

        var today = LocalDate.now();
        if (startDate.isBefore(today) || endDate.isBefore(today)){
            throw new BusinessLogicValidationException("las fechas son incorrectas");
        }
    }
}
