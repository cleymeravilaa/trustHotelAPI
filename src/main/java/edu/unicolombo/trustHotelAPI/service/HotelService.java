package edu.unicolombo.trustHotelAPI.service;

import edu.unicolombo.trustHotelAPI.domain.model.Hotel;
import edu.unicolombo.trustHotelAPI.domain.repository.EmployeeRepository;
import edu.unicolombo.trustHotelAPI.domain.repository.HotelRepository;
import edu.unicolombo.trustHotelAPI.dto.hotel.HotelDTO;
import edu.unicolombo.trustHotelAPI.dto.hotel.RegisterNewHotelDTO;
import edu.unicolombo.trustHotelAPI.dto.hotel.UpdateHotelDTO;
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
        Hotel hotel = new Hotel( data.name(), data.category(),data.address(),
                                data.phone(), data.floors());

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
}
