package edu.unicolombo.trustHotelAPI.service;

import edu.unicolombo.trustHotelAPI.domain.model.Employee;
import edu.unicolombo.trustHotelAPI.domain.model.Hotel;
import edu.unicolombo.trustHotelAPI.domain.repository.EmployeeRepository;
import edu.unicolombo.trustHotelAPI.domain.repository.HotelRepository;
import edu.unicolombo.trustHotelAPI.dto.employee.EmployeeDTO;
import edu.unicolombo.trustHotelAPI.dto.employee.RegisterNewEmployeeDTO;
import edu.unicolombo.trustHotelAPI.dto.employee.UpdateEmployeeDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    public EmployeeRepository employeeRepository;

    @Autowired
    public HotelRepository hotelRepository;

    public EmployeeDTO registerEmployee(RegisterNewEmployeeDTO data){
        Hotel hotel = hotelRepository.findById(data.hotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel no encontrado"));

        Employee savedEmployee = null;
        savedEmployee.setHotel(hotel);
        savedEmployee = employeeRepository.save(savedEmployee);
        return new EmployeeDTO(savedEmployee);
    }

    public Employee findById(Long id){
        return employeeRepository.getReferenceById(id);
    }

    public List<EmployeeDTO> getAllEmployees(){
        return employeeRepository.findAll()
                .stream().map(EmployeeDTO::new
                ).toList();
    }

    public List<EmployeeDTO> getAllEmployeesByHotel(Long hotelId){
        var hotel = hotelRepository.getReferenceById(hotelId);
        return employeeRepository.findByHotel(hotel)
                .stream().map(EmployeeDTO::new).toList();
    }

    public EmployeeDTO getEmployeeById(long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(EmployeeDTO::new).orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado"));
    }

    public void deleteById(long employeeId){
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado"));

        Hotel hotel = employee.getHotel();
        hotel.getEmployees().remove(employee);
        hotelRepository.save(hotel);
        employeeRepository.delete(employee);
    }

    @Transactional
    public EmployeeDTO updateEmployee(long employeeId, UpdateEmployeeDTO data){
        Employee  employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado"));



        if (!data.name().equals(employee.getName())){
            employee.setName(data.name());
        }

        if (!data.address().equals(employee.getAddress())){
            employee.setAddress(data.address());
        }
        // 4. Guardar cambios
        Employee updatedEmployee = employeeRepository.save(employee);
        return new EmployeeDTO(updatedEmployee);
    }
}
