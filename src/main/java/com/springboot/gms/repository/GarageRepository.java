package com.springboot.gms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.springboot.gms.entities.GarageEntity;

public interface GarageRepository extends JpaRepository<GarageEntity, Long>, JpaSpecificationExecutor<GarageEntity> {
	
}
