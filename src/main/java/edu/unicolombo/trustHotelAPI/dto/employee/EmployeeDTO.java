package edu.unicolombo.trustHotelAPI.dto.employee;

import edu.unicolombo.trustHotelAPI.domain.model.Employee;
import edu.unicolombo.trustHotelAPI.domain.model.enums.EmployeeType;

public record EmployeeDTO(long employeeId, String dni, String name,
                          String address, EmployeeType type, Long hotelId) {

    public EmployeeDTO(Employee employee){
        this(
                employee.getEmployeeId(),
                employee.getDni(),
                employee.getName(),
                employee.getAddress(),
                employee.getType(),
                employee.getHotel()!=null ? employee.getHotel().getHotelId() : null
        );
    }
}
