package com.springboot.gms.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.gms.entities.Accessory;
import com.springboot.gms.entities.Vehicle;
import com.springboot.gms.exception.ResourceNotFoundException;
import com.springboot.gms.repository.AccessoryRepository;
import com.springboot.gms.repository.VehicleRepository;
import com.springboot.gms.service.AccessoryService;

@Service
public class AccessoryServiceImpl implements AccessoryService {

	private AccessoryRepository accessoryRepository;
	private VehicleRepository vehicleRepository;

	public AccessoryServiceImpl(AccessoryRepository accessoryRepository, VehicleRepository vehicleRepository) {
		super();
		this.accessoryRepository = accessoryRepository;
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public Accessory addAccessoryToVehicle(Accessory accessory, Long vehicleId) {
		Vehicle vehicle = vehicleRepository.findById(vehicleId)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle", "ID", vehicleId));
		accessory.setVehicle(vehicle);
		return accessoryRepository.save(accessory);
	}

	@Override
	public Accessory updateAccessory(Accessory accessory, Long id) {
		Accessory existingAccessory = accessoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Accessory", "ID", id));
		existingAccessory.setName(accessory.getName());
		existingAccessory.setDescription(accessory.getDescription());
		existingAccessory.setPrice(accessory.getPrice());
		existingAccessory.setType(accessory.getType());

		if (accessory.getVehicle() != null && accessory.getVehicle().getId() != null) {
			Vehicle existingVehicle = vehicleRepository.findById(accessory.getVehicle().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Vehicle", "ID", accessory.getVehicle().getId()));
			accessory.setVehicle(existingVehicle);
		}

		return accessoryRepository.save(existingAccessory);
	}

	@Override
	public void deleteAccessory(Long id) {
		accessoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Accessory", "ID", id));
		accessoryRepository.deleteById(id);
	}

	@Override
	public Page<Accessory> getAccessoriesByVehicle(Long vehicleId, int pageIndex, int sizeOfPage) {
		Pageable sortedByNameAsc = PageRequest.of(pageIndex, sizeOfPage, Sort.by("name").ascending());
		// Check if Vehicle exists in DB or not
		vehicleRepository.findById(vehicleId)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle", "ID", vehicleId));
		return accessoryRepository.findByVehicleId(vehicleId, sortedByNameAsc);
	}

}
