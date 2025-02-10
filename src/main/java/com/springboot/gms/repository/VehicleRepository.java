package com.springboot.gms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.gms.entities.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	
	Page<Vehicle> findByGarageId(Long garageId, Pageable pageable);
	Page<Vehicle> findByBrand(String brand, Pageable pageable);

}
