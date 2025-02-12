package com.springboot.gms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.gms.entities.VehicleEntity;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
	
	Page<VehicleEntity> findByGarageId(Long garageId, Pageable pageable);
	Page<VehicleEntity> findByBrand(String brand, Pageable pageable);

}
