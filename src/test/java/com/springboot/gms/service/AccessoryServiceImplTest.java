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

import com.springboot.gms.entities.AccessoryEntity;
import com.springboot.gms.entities.VehicleEntity;
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
		VehicleEntity vehicle = VehicleEntity.builder().id(vehicleId).build();

		AccessoryEntity accessory = AccessoryEntity.builder().id(1L).name("GPS Tracker").build();

		Mockito.when(vehicleRepository.findById(Mockito.eq(vehicleId))).thenReturn(Optional.of(vehicle));
		Mockito.when(accessoryRepository.save(Mockito.any(AccessoryEntity.class))).thenReturn(accessory);

		// Act
		AccessoryEntity result = accessoryService.addAccessoryToVehicle(accessory, vehicleId);

		// Assert
		assertNotNull(result);
		assertEquals("GPS Tracker", result.getName());
		assertEquals(vehicleId, result.getVehicle().getId());

		// Verify method calls
		verify(vehicleRepository, times(1)).findById(vehicleId);
		verify(accessoryRepository, times(1)).save(Mockito.any(AccessoryEntity.class));
	}

	@Test
	void addAccessoryToVehicle_ShouldThrowException_WhenVehicleNotFound() {
		// Arrange
		Long vehicleId = 99L;
		Mockito.when(vehicleRepository.findById(Mockito.eq(vehicleId))).thenReturn(Optional.empty());

		AccessoryEntity accessory = AccessoryEntity.builder().id(1L).name("GPS Tracker").build();

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> {
			accessoryService.addAccessoryToVehicle(accessory, vehicleId);
		});

		// Verify method calls
		verify(vehicleRepository, times(1)).findById(vehicleId);
		verify(accessoryRepository, never()).save(any(AccessoryEntity.class));
	}

	@Test
	void updateAccessory_ShouldReturnAccessory() {
		// Arrange
		Long accessoryId = 1L;
		AccessoryEntity accessory = AccessoryEntity.builder().id(accessoryId).name("Car Cover").build();

		Mockito.when(accessoryRepository.findById(Mockito.eq(accessoryId))).thenReturn(Optional.of(accessory));
		Mockito.when(accessoryRepository.save(Mockito.any(AccessoryEntity.class))).thenReturn(accessory);

		// Act
		AccessoryEntity result = accessoryService.updateAccessory(accessory, accessoryId);

		// Assert
		assertNotNull(result);
		assertEquals("Car Cover", result.getName());

		// Verify method calls
		verify(accessoryRepository, times(1)).findById(accessoryId);
		verify(accessoryRepository, times(1)).save(Mockito.any(AccessoryEntity.class));
	}

	@Test
	void updateAccessory_ShouldThrowException_WhenAccessoryNotFound() {
		// Arrange
		Long accessoryId = 99L;
		AccessoryEntity accessory = AccessoryEntity.builder().id(accessoryId).build();
		
		Mockito.when(accessoryRepository.findById(Mockito.eq(accessoryId))).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> {
			accessoryService.updateAccessory(accessory, accessoryId);
		});

		// Verify method calls
		verify(accessoryRepository, times(1)).findById(accessoryId);
		verify(accessoryRepository, never()).save(any(AccessoryEntity.class));
	}

	@Test
	void deleteAccessory_ShouldDelete_WhenAccessoryExists() {
		// Arrange
		Long accessoryId = 1L;
		AccessoryEntity accessory = AccessoryEntity.builder().id(accessoryId).build();

		Mockito.when(accessoryRepository.findById(Mockito.eq(accessoryId))).thenReturn(Optional.of(accessory));
		Mockito.doNothing().when(accessoryRepository).deleteById(Mockito.eq(accessoryId));

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
		VehicleEntity vehicle = VehicleEntity.builder().id(1L).build();

		AccessoryEntity accessory1 = AccessoryEntity.builder().id(1L).name("Car Cover").vehicle(vehicle).build();
		AccessoryEntity accessory2 = AccessoryEntity.builder().id(2L).name("GPS Tracker").vehicle(vehicle).build();

		Page<AccessoryEntity> accessoryPage = new PageImpl<>(List.of(accessory1, accessory2));
		Pageable sortedByNameAsc = PageRequest.of(0, 5, Sort.by("name").ascending());

		Mockito.when(vehicleRepository.findById(Mockito.eq(vehicleId))).thenReturn(Optional.of(vehicle));
		Mockito.when(accessoryRepository.findByVehicleId(Mockito.eq(vehicleId), Mockito.eq(sortedByNameAsc)))
				.thenReturn(accessoryPage);

		// Act
		Page<AccessoryEntity> result = accessoryService.getAccessoriesByVehicle(vehicleId, 0, 5);

		// Assert
		assertNotNull(result);
		assertEquals(2, result.getContent().size());
		assertEquals("Car Cover", result.getContent().get(0).getName());

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
