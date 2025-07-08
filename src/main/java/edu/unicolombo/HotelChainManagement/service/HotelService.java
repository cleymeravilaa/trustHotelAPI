package edu.unicolombo.HotelChainManagement.service;

import edu.unicolombo.HotelChainManagement.domain.model.Employee;
import edu.unicolombo.HotelChainManagement.domain.model.EmployeeType;
import edu.unicolombo.HotelChainManagement.domain.model.Hotel;
import edu.unicolombo.HotelChainManagement.domain.repository.EmployeeRepository;
import edu.unicolombo.HotelChainManagement.domain.repository.HotelRepository;
import edu.unicolombo.HotelChainManagement.dto.hotel.ChangeDirectorDTO;
import edu.unicolombo.HotelChainManagement.dto.hotel.HotelDTO;
import edu.unicolombo.HotelChainManagement.dto.hotel.RegisterNewHotelDTO;
import edu.unicolombo.HotelChainManagement.dto.hotel.UpdateHotelDTO;
import edu.unicolombo.HotelChainManagement.infrastructure.errors.exception.BusinessLogicValidationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    @Autowired
    public HotelRepository hotelRepository;

    @Autowired
    public EmployeeRepository employeeRepository;

    public HotelDTO register(RegisterNewHotelDTO data){
        Hotel hotel = new Hotel();
        hotel.setName(data.name());
        hotel.setAddress(data.address());
        hotel.setPhone(data.phone());
        hotel.setCategory(data.category());

        Hotel savedHotel = hotelRepository.save(hotel);

        return new HotelDTO(savedHotel);
    }

    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(HotelDTO::new)
                .toList();
    }

    public HotelDTO getHotelById(Long id) {
        return hotelRepository.findById(id)
                .map(HotelDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Hotel no encontrado"));
    }

    @Transactional
    public void deleteById(long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel no encontrado"));

        // Primero desvincular a todos los empleados
        hotel.getEmployees().forEach(employee -> employee.setHotel(null));
        if (hotel.getDirector() != null) {
            hotel.getDirector().setHotel(null);
        }
        hotelRepository.delete(hotel);
    }

    public HotelDTO updateHotel(long hotelId, UpdateHotelDTO data) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel no encontrado"));

        System.out.println("Hotel encontrado: "+ hotel.getName());

        hotel.setName(data.name());
        hotel.setAddress(data.address());
        hotel.setPhone(data.phone());
        hotel.setCategory(data.category());

        Hotel updatedHotel = hotelRepository.save(hotel);
        return new HotelDTO(updatedHotel);
    }

    public HotelDTO changeDirector(ChangeDirectorDTO data) {
        Hotel hotel = hotelRepository.getReferenceById(data.hotelId());
        Employee director = employeeRepository.getReferenceById(data.directorId());

        if (!director.getType().equals(EmployeeType.DIRECTOR)){
            throw new BusinessLogicValidationException("No se puede realizar la operacion el empleado no es de tipo director");
        }

        director.getHotel().setDirector(null);
        hotel.setDirector(director);
        director.setHotel(hotel);

        employeeRepository.save(director);
        return new HotelDTO(hotelRepository.save(hotel));
    }
}
