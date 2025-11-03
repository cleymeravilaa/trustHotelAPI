package edu.unicolombo.trustHotelAPI.domain.model;

import edu.unicolombo.trustHotelAPI.dto.customer.RegisterNewCustomerDTO;
import edu.unicolombo.trustHotelAPI.dto.customer.UpdateCustomerDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "customerId")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String dni;
    private String name;
    private String address;
    private String phone;

    public Customer (String dni, String name, String address, String phone) {
        this.dni = dni;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Customer(RegisterNewCustomerDTO data) {
        this.dni = data.dni();
        this.name = data.name();
        this.address = data.address();
        this.phone = data.phone();
    }

    public void updateData(UpdateCustomerDTO data) {
        if (data.name()!=null) {
            this.name = data.name();
        }
        if (data.phone()!=null) {
            this.phone = data.phone();
        }
        if (data.address()!=null) {
            this.address = data.address();
        }
    }
}
