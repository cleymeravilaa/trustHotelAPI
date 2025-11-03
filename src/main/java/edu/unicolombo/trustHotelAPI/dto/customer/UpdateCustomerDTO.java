package edu.unicolombo.trustHotelAPI.dto.customer;

import edu.unicolombo.trustHotelAPI.domain.model.Customer;

public record UpdateCustomerDTO(String name, String address, String phone) {

    public UpdateCustomerDTO(Customer customer) {
        this(customer.getName(), customer.getAddress(), customer.getPhone());
    }
}
