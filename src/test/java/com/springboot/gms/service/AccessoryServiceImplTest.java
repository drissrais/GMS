package com.springboot.gms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.springboot.gms.entities.Accessory;
import com.springboot.gms.entities.Vehicle;
import com.springboot.gms.exception.ResourceNotFoundException;
import com.springboot.gms.repository.AccessoryRepository;
import com.springboot.gms.repository.VehicleRepository;
import com.springboot.gms.service.impl.AccessoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AccessoryServiceImplTest {

	@Mock
	private VehicleRepository vehicleRepository;

	@Mock
	private AccessoryRepository accessoryRepository;

	@InjectMocks
	private AccessoryServiceImpl accessoryService;

	@Test
	void addAccessoryToVehicle_ShouldReturnAccessory() {
		// Arrange
		Long vehicleId = 1L;
		Vehicle vehicle = new Vehicle();
		vehicle.setId(vehicleId);

		Accessory accessory = new Accessory();
		accessory.setId(1L);
		accessory.setName("GPS Tracker");
		accessory.setVehicle(vehicle);

		Mockito.when(vehicleRepository.findById(Mockito.eq(vehicleId))).thenReturn(Optional.of(vehicle));
		Mockito.when(accessoryRepository.save(Mockito.any(Accessory.class))).thenReturn(accessory);

		// Act
		Accessory result = accessoryService.addAccessoryToVehicle(accessory, vehicleId);

		// Assert
		assertNotNull(result);
		assertEquals("GPS Tracker", result.getName());
		assertEquals(vehicleId, result.getVehicle().getId());

		// Verify method calls
		verify(vehicleRepository, times(1)).findById(vehicleId);
		verify(accessoryRepository, times(1)).save(Mockito.any(Accessory.class));
	}

	@Test
	void addAccessoryToVehicle_ShouldThrowException_WhenVehicleNotFound() {
		// Arrange
		Long vehicleId = 99L;
		Mockito.when(vehicleRepository.findById(Mockito.eq(vehicleId))).thenReturn(Optional.empty());

		Accessory accessory = new Accessory();
		accessory.setId(1L);
		accessory.setName("GPS Tracker");

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> {
			accessoryService.addAccessoryToVehicle(accessory, vehicleId);
		});

		// Verify method calls
		verify(vehicleRepository, times(1)).findById(vehicleId);
		verify(accessoryRepository, never()).save(any(Accessory.class));
	}

	@Test
	void updateAccessory_ShouldReturnAccessory() {
		// Arrange
		Vehicle vehicle = new Vehicle();
		vehicle.setId(1L);

		Long accessoryId = 1L;
		Accessory accessory = new Accessory();
		accessory.setId(accessoryId);
		accessory.setName("Car Cover");
		accessory.setDescription("Waterproof and dustproof car cover for sedans.");
		accessory.setPrice(49.99);
		accessory.setType("Protection");
		accessory.setVehicle(vehicle);

		Mockito.when(accessoryRepository.findById(Mockito.eq(accessoryId))).thenReturn(Optional.of(accessory));
		Mockito.when(vehicleRepository.findById(Mockito.eq(accessory.getVehicle().getId())))
				.thenReturn(Optional.of(vehicle));
		Mockito.when(accessoryRepository.save(Mockito.any(Accessory.class))).thenReturn(accessory);

		// Act
		Accessory result = accessoryService.updateAccessory(accessory, accessoryId);

		// Assert
		assertNotNull(result);
		assertEquals("Car Cover", result.getName());
		assertEquals(vehicle.getId(), result.getVehicle().getId());

		// Verify method calls
		verify(accessoryRepository, times(1)).findById(accessoryId);
		verify(vehicleRepository, times(1)).findById(accessory.getVehicle().getId());
		verify(accessoryRepository, times(1)).save(Mockito.any(Accessory.class));
	}

	@Test
	void updateAccessory_ShouldThrowException_WhenAccessoryNotFound() {
		// Arrange
		Long accessoryId = 99L;
		Accessory accessory = new Accessory();
		accessory.setId(accessoryId);
		Mockito.when(accessoryRepository.findById(Mockito.eq(accessoryId))).thenReturn(Optional.empty());

		Vehicle vehicle = new Vehicle();
		vehicle.setId(1L);

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> {
			accessoryService.updateAccessory(accessory, accessoryId);
		});

		// Verify method calls
		verify(accessoryRepository, times(1)).findById(accessoryId);
		verify(vehicleRepository, never()).findById(vehicle.getId());
		verify(accessoryRepository, never()).save(any(Accessory.class));
	}

	@Test
	void deleteAccessory_ShouldDelete_WhenAccessoryExists() {
		// Arrange
		Long accessoryId = 1L;
		Accessory accessory = new Accessory();
		accessory.setId(accessoryId);

		Mockito.when(accessoryRepository.findById(Mockito.eq(accessoryId))).thenReturn(Optional.of(accessory));

		// Act
		accessoryService.deleteAccessory(accessoryId);

		// Assert
		verify(accessoryRepository, times(1)).findById(accessoryId);
		verify(accessoryRepository, times(1)).deleteById(accessoryId);
	}

	@Test
	void deleteAccessory_ShouldThrowException_WhenAccessoryNotFond() {
		// Arrange
		Long accessoryId = 99L;
		Mockito.when(accessoryRepository.findById(Mockito.eq(accessoryId))).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> {
			accessoryService.deleteAccessory(accessoryId);
		});

		// Verify method calls
		verify(accessoryRepository, times(1)).findById(accessoryId);
		verify(accessoryRepository, never()).deleteById(accessoryId);
	}

	@Test
	void getAccessoriesByVehicle_ShouldReturnPageOfAccessories() {
		// Arrange
		Long vehicleId = 1L;
		Vehicle vehicle = new Vehicle();
		vehicle.setId(vehicleId);

		Accessory accessory1 = new Accessory();
		accessory1.setId(1L);
		accessory1.setName("GPS Tracker");
		accessory1.setVehicle(vehicle);

		Accessory accessory2 = new Accessory();
		accessory2.setId(2L);
		accessory2.setName("Car Cover");
		accessory2.setVehicle(vehicle);

		Page<Accessory> accessoryPage = new PageImpl<>(List.of(accessory1, accessory2));
		Pageable sortedByNameAsc = PageRequest.of(0, 5, Sort.by("name").ascending());

		Mockito.when(vehicleRepository.findById(Mockito.eq(vehicleId))).thenReturn(Optional.of(vehicle));
		Mockito.when(accessoryRepository.findByVehicleId(Mockito.eq(vehicleId), sortedByNameAsc))
				.thenReturn(accessoryPage);

		// Act
		Page<Accessory> result = accessoryService.getAccessoriesByVehicle(vehicleId, 0, 5);

		// Assert
		assertNotNull(result);
		assertEquals(2, result.getContent().size());
		assertEquals("GPS Tracker", result.getContent().get(0).getName());

		verify(vehicleRepository, times(1)).findById(vehicleId);
		verify(accessoryRepository, times(1)).findByVehicleId(vehicleId, sortedByNameAsc);
	}

	@Test
	void getAccessoriesByVehicle_ShouldThrowException_WhenVehicleNotFound() {
		// Arrange
		Long vehicleId = 99L;
		when(vehicleRepository.findById(Mockito.eq(vehicleId))).thenReturn(Optional.empty());

		Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> {
			accessoryService.getAccessoriesByVehicle(vehicleId, 0, 5);
		});

		// Verify method calls
		verify(vehicleRepository, times(1)).findById(vehicleId);
		verify(accessoryRepository, never()).findByVehicleId(vehicleId, pageable);
	}

}
