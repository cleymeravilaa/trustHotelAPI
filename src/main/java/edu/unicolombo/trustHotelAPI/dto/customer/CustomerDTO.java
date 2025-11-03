package edu.unicolombo.trustHotelAPI.dto.customer;

import edu.unicolombo.trustHotelAPI.domain.model.Customer;

public record CustomerDTO(Long customerId, String dni, String name, String address, String phone) {

    public CustomerDTO(Customer customer) {
        this(customer.getCustomerId(), customer.getDni(), customer.getName(),
                customer.getAddress(), customer.getPhone());
    }
}
