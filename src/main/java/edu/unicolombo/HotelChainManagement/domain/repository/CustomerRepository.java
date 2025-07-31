package edu.unicolombo.HotelChainManagement.domain.repository;

import edu.unicolombo.HotelChainManagement.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer getCustomerByDni(String customerDni);
}
