package com.springboot.gms.service.impl;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.gms.entities.GarageEntity;
import com.springboot.gms.entities.VehicleEntity;
import com.springboot.gms.exception.ResourceNotFoundException;
import com.springboot.gms.repository.GarageRepository;
import com.springboot.gms.repository.VehicleRepository;
import com.springboot.gms.service.VehicleService;

@Service
public class VehicleServiceImpl implements VehicleService {

	private VehicleRepository vehicleRepository;
	private GarageRepository garageRepository;

	public VehicleServiceImpl(VehicleRepository vehicleRepository, GarageRepository garageRepository) {
		super();
		this.vehicleRepository = vehicleRepository;
		this.garageRepository = garageRepository;
	}

	@Override
	public VehicleEntity addVehicleToGarage(Long garageId, VehicleEntity vehicle) {
		GarageEntity garage = garageRepository.findById(garageId)
				.orElseThrow(() -> new ResourceNotFoundException("Garage", "ID", garageId));

		// Validate that Garage contains less than 50 vehicles
		if (garage.getVehicles().size() >= 50) {
			throw new IllegalStateException("Garage cannot store more than 50 vehicles.");
		}

		vehicle.setGarage(garage);
		return vehicleRepository.save(vehicle);
	}

	@Override
	public Page<VehicleEntity> getVehiclesByGarage(Long garageId, int pageIndex, int sizeOfPage) {
		Pageable sortedByBrandAsc = PageRequest.of(pageIndex, sizeOfPage, Sort.by("brand").ascending());
		// Check if Garage exists in DB or not
		garageRepository.findById(garageId).orElseThrow(() -> new ResourceNotFoundException("Garage", "Id", garageId));
		return vehicleRepository.findByGarageId(garageId, sortedByBrandAsc);
	}

	@Override
	public Page<VehicleEntity> getVehiclesByBrand(String brand, int pageIndex, int sizeOfPage) {
		Pageable sortedByBrandAsc = PageRequest.of(pageIndex, sizeOfPage, Sort.by("brand").ascending());
		return vehicleRepository.findByBrand(brand, sortedByBrandAsc);
	}

	@Override
	public VehicleEntity updateVehicle(VehicleEntity vehicle, Long id) {
		VehicleEntity existingVehicle = vehicleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle", "ID", id));
		existingVehicle.setBrand(vehicle.getBrand());
		existingVehicle.setManufacturingYear(vehicle.getManufacturingYear());
		existingVehicle.setFuelType(vehicle.getFuelType());

		if (Objects.nonNull(vehicle.getGarage()) && Objects.nonNull(vehicle.getGarage().getId())) {
			GarageEntity existingGarage = garageRepository.findById(vehicle.getGarage().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Garage", "ID", vehicle.getGarage().getId()));
			existingVehicle.setGarage(existingGarage);
		}

		return vehicleRepository.save(existingVehicle);
	}

	@Override
	public void deleteVehicle(Long id) {
		vehicleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle", "ID", id));
		vehicleRepository.deleteById(id);
	}

}
