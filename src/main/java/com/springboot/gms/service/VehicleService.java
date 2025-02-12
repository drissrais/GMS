package com.springboot.gms.service;

import org.springframework.data.domain.Page;

import com.springboot.gms.entities.VehicleEntity;

public interface VehicleService {
	
	VehicleEntity addVehicleToGarage(Long garageId, VehicleEntity vehicle);
	Page<VehicleEntity> getVehiclesByGarage(Long garageId, int pageIndex, int sizeOfPage);
	Page<VehicleEntity> getVehiclesByBrand(String brand, int pageIndex, int sizeOfPage);
	VehicleEntity updateVehicle(VehicleEntity vehicle, Long id);
	void deleteVehicle(Long id);

}
