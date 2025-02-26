package com.springboot.gms.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.springboot.gms.entities.DailyOpeningTimeEntity;
import com.springboot.gms.entities.GarageEntity;
import com.springboot.gms.entities.OpeningTimeEntity;
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
	public GarageEntity saveGarage(GarageEntity garage) {
		if (Objects.nonNull(garage.getDailyOpeningTimeList())) {
			for (DailyOpeningTimeEntity dailyOpeningTime : garage.getDailyOpeningTimeList()) {
				dailyOpeningTime.setGarage(garage);
				if (Objects.nonNull(dailyOpeningTime.getOpeningTimes())) {
					for (OpeningTimeEntity openingTime : dailyOpeningTime.getOpeningTimes()) {
						openingTime.setDailyOpeningTime(dailyOpeningTime);
					}
				}
			}
		}
		return garageRepository.save(garage);
	}

	@Override
	public List<GarageEntity> getAllGarages() {
		return garageRepository.findAll();
	}

	@Override
	public GarageEntity getGarageById(Long id) {
		return garageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Garage", "ID", id));
	}

	@Override
	public GarageEntity updateGarage(GarageEntity garage, Long id) {
		GarageEntity existingGarage = garageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Garage", "ID", id));
		existingGarage.setName(garage.getName());
		existingGarage.setAddress(garage.getAddress());
		existingGarage.setCity(garage.getCity());
		existingGarage.setPhoneNumber(garage.getPhoneNumber());
		existingGarage.setEmail(garage.getEmail());

		if (Objects.nonNull(garage.getDailyOpeningTimeList())) {
			existingGarage.getDailyOpeningTimeList().clear();
			for (DailyOpeningTimeEntity dailyOpeningTime : garage.getDailyOpeningTimeList()) {
				dailyOpeningTime.setGarage(existingGarage);
				if (Objects.nonNull(dailyOpeningTime.getOpeningTimes())) {
					for (OpeningTimeEntity openingTime : dailyOpeningTime.getOpeningTimes()) {
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
	public Page<GarageEntity> getGarageListByCriteria(String name, String city, int pageIndex, int sizeOfPage) {
		Pageable sortedByNameAsc = PageRequest.of(pageIndex, sizeOfPage, Sort.by("name").ascending());
		Specification<GarageEntity> spec = GarageSpecification.findByCriteria(name, city);
		return garageRepository.findAll(spec, sortedByNameAsc);
	}

}
