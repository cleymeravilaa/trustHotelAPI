package edu.unicolombo.trustHotelAPI.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.unicolombo.trustHotelAPI.domain.model.Staying;

public interface StayingRepository extends JpaRepository<Staying, Long> {

}
