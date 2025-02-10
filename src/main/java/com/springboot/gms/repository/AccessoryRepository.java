package com.springboot.gms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.gms.entities.Accessory;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

	Page<Accessory> findByVehicleId(Long vehicleId, Pageable pageable);
	
}
