package com.springboot.gms.service;

import org.springframework.data.domain.Page;

import com.springboot.gms.entities.Vehicle;

public interface VehicleService {
	
	Vehicle addVehicleToGarage(Long garageId, Vehicle vehicle);
	Page<Vehicle> getVehiclesByGarage(Long garageId, int pageIndex, int sizeOfPage);
	Page<Vehicle> getVehiclesByBrand(String brand, int pageIndex, int sizeOfPage);
	Vehicle updateVehicle(Vehicle vehicle, Long id);
	void deleteVehicle(Long id);

}
