package com.springboot.gms.service;

import org.springframework.data.domain.Page;

import com.springboot.gms.entities.AccessoryEntity;

public interface AccessoryService {
	
	AccessoryEntity addAccessoryToVehicle(AccessoryEntity accessory, Long vehicleId);
	AccessoryEntity updateAccessory(AccessoryEntity accessory, Long id);
	void deleteAccessory(Long id);
	Page<AccessoryEntity> getAccessoriesByVehicle(Long vehicleId, int pageIndex, int sizeOfPage);

}
