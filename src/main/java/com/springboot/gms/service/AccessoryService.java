package com.springboot.gms.service;

import org.springframework.data.domain.Page;

import com.springboot.gms.entities.Accessory;

public interface AccessoryService {
	
	Accessory addAccessoryToVehicle(Accessory accessory, Long vehicleId);
	Accessory updateAccessory(Accessory accessory, Long id);
	void deleteAccessory(Long id);
	Page<Accessory> getAccessoriesByVehicle(Long vehicleId, int pageIndex, int sizeOfPage);

}
