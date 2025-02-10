package com.springboot.gms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.springboot.gms.entities.Garage;

public interface GarageService {
	
	Garage saveGarage(Garage garage);
	List<Garage> getAllGarages();
	Garage getGarageById(Long id);
	Garage updateGarage(Garage garage, Long id);
	void deleteGarage(Long id);
	Page<Garage> getGarageListByCriteria(String name, String city, int pageIndex, int sizeOfPage);

}
