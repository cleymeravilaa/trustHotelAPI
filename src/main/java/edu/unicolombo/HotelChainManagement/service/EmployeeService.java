package edu.unicolombo.HotelChainManagement.service;

import edu.unicolombo.HotelChainManagement.domain.model.Employee;
import edu.unicolombo.HotelChainManagement.domain.model.EmployeeType;
import edu.unicolombo.HotelChainManagement.domain.model.Hotel;
import edu.unicolombo.HotelChainManagement.domain.repository.EmployeeRepository;
import edu.unicolombo.HotelChainManagement.domain.repository.HotelRepository;
import edu.unicolombo.HotelChainManagement.dto.employee.ChangeHotelDTO;
import edu.unicolombo.HotelChainManagement.dto.employee.EmployeeDTO;
import edu.unicolombo.HotelChainManagement.dto.employee.RegisterNewEmployeeDTO;
import edu.unicolombo.HotelChainManagement.dto.employee.UpdateEmployeeDTO;
import edu.unicolombo.HotelChainManagement.infrastructure.errors.exception.BusinessLogicValidationException;
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
        if (data.type().equals(EmployeeType.DIRECTOR)){
            if (hotel.getDirector() != null){
                throw new BusinessLogicValidationException("No se puede asignar un director a este hotel");
            }
            savedEmployee  = employeeRepository.save(new Employee(data));
            hotel.setDirector(savedEmployee);
        } else {
            savedEmployee  = employeeRepository.save(new Employee(data));
            hotel.getEmployees().add(savedEmployee);
        }
        savedEmployee.setHotel(hotel);
        savedEmployee = employeeRepository.save(savedEmployee);
        hotelRepository.save(hotel);
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

        if (employee.getType() == EmployeeType.DIRECTOR) {
            // Si es director, eliminamos la referencia
            hotel.setDirector(null);
        } else {
            // Si es empleado normal, lo removemos de la lista
            hotel.getEmployees().remove(employee);
        }
        employeeRepository.delete(employee);
    }

    @Transactional
    public EmployeeDTO updateEmployee(long employeeId, UpdateEmployeeDTO data){
        Employee  employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado"));

        if (!data.type().equals(employee.getType())){
            if (employee.getType().equals(EmployeeType.DIRECTOR)){
                employee.getHotel().setDirector(null);
                employee.setType(data.type());
            } else {
                if(data.type().equals(EmployeeType.DIRECTOR)){
                    if (employee.getHotel().getDirector() != null){
                        throw new BusinessLogicValidationException("No se puede colocar el empleado como director");
                    }
                    employee.setType(EmployeeType.DIRECTOR);
                    employee.getHotel().setDirector(employee);
                }
                employee.setType(data.type());
            }
        }

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

    @Transactional
    public EmployeeDTO changeHotel(ChangeHotelDTO data){
        Employee employee = employeeRepository.getReferenceById(data.employeeId());
        Hotel hotel = hotelRepository.getReferenceById(data.hotelToChangeId());

        if (employee.getType().equals(EmployeeType.DIRECTOR)){
            throw new BusinessLogicValidationException("No se puede realizar el cambio de hotel. El empleado es de tipo director");
        } else {
            if (!employee.getHotel().equals(hotel)){
                employee.getHotel().getEmployees().remove(null);
                hotel.getEmployees().add(employee);
                employee.setHotel(hotel);
            }
        }
        hotelRepository.save(hotel);
        return new EmployeeDTO(employeeRepository.save(employee));
    }
}
