package com.springboot.gms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.springboot.gms.entities.GarageEntity;

public interface GarageService {
	
	GarageEntity saveGarage(GarageEntity garage);
	List<GarageEntity> getAllGarages();
	GarageEntity getGarageById(Long id);
	GarageEntity updateGarage(GarageEntity garage, Long id);
	void deleteGarage(Long id);
	Page<GarageEntity> getGarageListByCriteria(String name, String city, int pageIndex, int sizeOfPage);

}
