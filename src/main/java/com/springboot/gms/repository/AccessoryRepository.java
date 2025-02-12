package com.springboot.gms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.gms.entities.AccessoryEntity;

public interface AccessoryRepository extends JpaRepository<AccessoryEntity, Long> {

	Page<AccessoryEntity> findByVehicleId(Long vehicleId, Pageable pageable);
	
}
