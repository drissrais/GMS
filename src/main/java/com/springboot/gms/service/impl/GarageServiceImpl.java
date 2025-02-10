package com.springboot.gms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.springboot.gms.entities.DailyOpeningTime;
import com.springboot.gms.entities.Garage;
import com.springboot.gms.entities.OpeningTime;
import com.springboot.gms.exception.ResourceNotFoundException;
import com.springboot.gms.repository.GarageRepository;
import com.springboot.gms.service.GarageService;
import com.springboot.gms.utils.GarageSpecification;

@Service
public class GarageServiceImpl implements GarageService {

	private GarageRepository garageRepository;

	public GarageServiceImpl(GarageRepository garageRepository) {
		super();
		this.garageRepository = garageRepository;
	}

	@Override
	public Garage saveGarage(Garage garage) {
		if (garage.getDailyOpeningTimeList() != null) {
			for (DailyOpeningTime dailyOpeningTime : garage.getDailyOpeningTimeList()) {
				dailyOpeningTime.setGarage(garage);
				if (dailyOpeningTime.getOpeningTimes() != null) {
					for (OpeningTime openingTime : dailyOpeningTime.getOpeningTimes()) {
						openingTime.setDailyOpeningTime(dailyOpeningTime);
					}
				}
			}
		}
		return garageRepository.save(garage);
	}

	@Override
	public List<Garage> getAllGarages() {
		return garageRepository.findAll();
	}

	@Override
	public Garage getGarageById(Long id) {
		return garageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Garage", "ID", id));
	}

	@Override
	public Garage updateGarage(Garage garage, Long id) {
		Garage existingGarage = garageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Garage", "ID", id));
		existingGarage.setName(garage.getName());
		existingGarage.setAddress(garage.getAddress());
		existingGarage.setCity(garage.getCity());
		existingGarage.setPhoneNumber(garage.getPhoneNumber());
		existingGarage.setEmail(garage.getEmail());

		if (garage.getDailyOpeningTimeList() != null) {
			existingGarage.getDailyOpeningTimeList().clear();
			for (DailyOpeningTime dailyOpeningTime : garage.getDailyOpeningTimeList()) {
				dailyOpeningTime.setGarage(existingGarage);
				if (dailyOpeningTime.getOpeningTimes() != null) {
					for (OpeningTime openingTime : dailyOpeningTime.getOpeningTimes()) {
						openingTime.setDailyOpeningTime(dailyOpeningTime);
					}
				}
				existingGarage.getDailyOpeningTimeList().add(dailyOpeningTime);
			}
		}
		return garageRepository.save(existingGarage);
	}

	@Override
	public void deleteGarage(Long id) {
		garageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Garage", "ID", id));
		garageRepository.deleteById(id);
	}

	@Override
	public Page<Garage> getGarageListByCriteria(String name, String city, int pageIndex, int sizeOfPage) {
		Pageable sortedByNameAsc = PageRequest.of(pageIndex, sizeOfPage, Sort.by("name").ascending());
		Specification<Garage> spec = GarageSpecification.findByCriteria(name, city);
		return garageRepository.findAll(spec, sortedByNameAsc);
	}

}
