package edu.unicolombo.trustHotelAPI.controller;

import edu.unicolombo.trustHotelAPI.dto.employee.EmployeeDTO;
import edu.unicolombo.trustHotelAPI.dto.employee.RegisterNewEmployeeDTO;
import edu.unicolombo.trustHotelAPI.dto.employee.UpdateEmployeeDTO;
import edu.unicolombo.trustHotelAPI.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @Autowired
    public EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDTO> registerEmployee(@RequestBody RegisterNewEmployeeDTO data, UriComponentsBuilder uriBuilder){
        var registeredEmployee = employeeService.registerEmployee(data);
        URI url = uriBuilder.path("/employees/{employeeId}").buildAndExpand(registeredEmployee.employeeId()).toUri();
        return ResponseEntity.created(url).body(registeredEmployee);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesByHotel(@PathVariable long hotelId){
        return ResponseEntity.ok(employeeService.getAllEmployeesByHotel(hotelId));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable long employeeId){
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }

    @DeleteMapping("/{employeeId}")
    @Transactional
    public ResponseEntity<Void> deleteEmployee(@PathVariable long employeeId){
        employeeService.deleteById(employeeId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{employeeId}")
    @Transactional
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable long employeeId,@RequestBody UpdateEmployeeDTO data){
        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, data));
    }

}
