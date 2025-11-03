package edu.unicolombo.trustHotelAPI.domain.model;

import edu.unicolombo.trustHotelAPI.domain.model.enums.EmployeeDepartment;
import edu.unicolombo.trustHotelAPI.domain.model.enums.EmployeeType;
import edu.unicolombo.trustHotelAPI.dto.employee.RegisterNewEmployeeDTO;
import edu.unicolombo.trustHotelAPI.dto.employee.UpdateEmployeeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "Employee")
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "employeeId")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    private String dni;
    private String email;
    private String name;
    private String address;
    @Enumerated(EnumType.STRING)
    private EmployeeType type;
    @Enumerated(EnumType.STRING)
    private EmployeeDepartment department;
    private String phone;
    // Relacion ManyToOne cn Hotel (para empleados normales)
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    // @OneToOne
    // @JoinColumn(name = "user_id")
    // private User user;

    public Employee(String dni, String name, String address,
                        EmployeeType type, EmployeeDepartment department, String phone ) {
        this.dni= dni;
        this.name = name;
        this.address = address;
        this.type = type;
        this.department = department;
        this.phone = phone;
    }

    public Employee(RegisterNewEmployeeDTO data){
        this.dni = data.dni();
        this.name = data.name();
        this.address = data.address();
        this.type = data.type();
    }

    public void updateData(UpdateEmployeeDTO data){
        if (data.name()!=null) {
            this.name = data.name();
        }

        if (data.address()!=null) {
            this.address = data.address(); 
        }

        if (data.type() != null) {
            this.type = data.type();
        }
    }

}
