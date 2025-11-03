package edu.unicolombo.trustHotelAPI.dto.employee;

import edu.unicolombo.trustHotelAPI.domain.model.enums.EmployeeType;

public record UpdateEmployeeDTO(
        String name,
        String address,
        EmployeeType type) {

}
