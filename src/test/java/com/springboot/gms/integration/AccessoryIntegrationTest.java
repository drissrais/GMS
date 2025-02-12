package com.springboot.gms.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.springboot.gms.entities.AccessoryEntity;
import com.springboot.gms.entities.GarageEntity;
import com.springboot.gms.entities.VehicleEntity;
import com.springboot.gms.repository.AccessoryRepository;
import com.springboot.gms.repository.GarageRepository;
import com.springboot.gms.repository.VehicleRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AccessoryIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private AccessoryRepository accessoryRepository;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private GarageRepository garageRepository;
	
	@Test
	void addAccessoryToVehicle_ShouldReturnCreatedAccessory() throws Exception {
		// Arrange
		GarageEntity garage = new GarageEntity();
		garage.setName("Garage AutoTech");
		garage = garageRepository.save(garage);
		
		VehicleEntity vehicle = new VehicleEntity();
		vehicle.setBrand("Talisman");
		vehicle.setGarage(garage);
		vehicleRepository.save(vehicle);
		
		String accessoryJson = """
			{
				"name": "Sunroof Cover",
				"description": "Protects sunroof from dust",
				"price": 50.0,
				"type": "Exterior"
			}
		""";
		
		// Act & Assert
		mockMvc.perform(post("/api/accessory/add/{vehicleId}", vehicle.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(accessoryJson))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name").value("Sunroof Cover"));
	}
	
	@Test
	void addAccessoryToNonExistantVehicle_ShouldReturnVehicleDoesNotExist() throws Exception {
		// Arrange
		Long nonExistantVehicleId = 999L;
		
		String accessoryJson = """
			{
				"name": "Sunroof Cover",
				"description": "Protects sunroof from dust",
				"price": 50.0,
				"type": "Exterior"
			}
		""";
		
		mockMvc.perform(post("/api/accessory/add/{vehicleId}", nonExistantVehicleId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(accessoryJson))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void updateAccessory_ShouldReturnUpdatedAccessory() throws Exception {
		// Arrange
		GarageEntity garage = new GarageEntity();
		garage.setName("Garage AutoTech");
		garage = garageRepository.save(garage);
		
		VehicleEntity vehicle = new VehicleEntity();
		vehicle.setBrand("Talisman");
		vehicle.setGarage(garage);
		vehicle = vehicleRepository.save(vehicle);
		
		AccessoryEntity accessory = new AccessoryEntity();
		accessory.setName("Sunroof Cover");
		accessory.setDescription("Protects sunroof from dust");
		accessory.setPrice(50.0);
		accessory.setType("Exterior");
		accessory.setVehicle(vehicle);
		accessory = accessoryRepository.save(accessory);
		
		String accessoryJson = """
			{
				"name": "Sunroof Cover",
				"description": "Protects sunroof from dust",
				"price": 60.0,
				"type": "Exterior"
			}	
		""";
		
		// Act & Assert
		mockMvc.perform(put("/api/accessory/update/{accessoryId}", accessory.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(accessoryJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.price").value(60.0));
	}
	
	@Test
	void updateAccessory_ShouldReturnAccessoryDoesNotExist() throws Exception {
		// Arrange
		Long nonExistantAccessoryId = 999L;
		
		String accessoryJson = """
			{
				"name": "Sunroof Cover",
				"description": "Protects sunroof from dust",
				"price": 60.0,
				"type": "Exterior"
			}	
		""";
		
		// Act & Assert
		mockMvc.perform(put("/api/accessory/update/{accessoryId}", nonExistantAccessoryId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(accessoryJson))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void deleteAccessory_ShouldDelete() throws Exception {
		// Arrange
		GarageEntity garage = new GarageEntity();
		garage.setName("Garage AutoTech");
		garage = garageRepository.save(garage);
		
		VehicleEntity vehicle = new VehicleEntity();
		vehicle.setBrand("Talisman");
		vehicle.setGarage(garage);
		vehicle = vehicleRepository.save(vehicle);
		
		AccessoryEntity accessory = new AccessoryEntity();
		accessory.setName("Sunroof Cover");
		accessory.setDescription("Protects sunroof from dust");
		accessory.setPrice(50.0);
		accessory.setType("Exterior");
		accessory.setVehicle(vehicle);
		accessory = accessoryRepository.save(accessory);
		
		// Act & Assert
		mockMvc.perform(delete("/api/accessory/delete/{accessoryId}", accessory.getId()))
			.andExpect(status().isOk())
			.andExpect(content().string("Accessory deleted successfully."));
	}
	
	@Test
	void deleteAccessory_ShouldReturnAccessoryDoesNotExist() throws Exception {
		// Arrange
		Long nonExistantAccessoryId = 999L;
		
		// Act & Assert
		mockMvc.perform(delete("/api/accessory/delete/{accessoryId}", nonExistantAccessoryId))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void getAccessoriesByVehicle_ShouldReturnPageOfAccessories() throws Exception {
		// Arrange
		GarageEntity garage = new GarageEntity();
		garage.setName("Garage AutoTech");
		garage = garageRepository.save(garage);
		
		VehicleEntity vehicle = new VehicleEntity();
		vehicle.setBrand("Talisman");
		vehicle.setGarage(garage);
		vehicle = vehicleRepository.save(vehicle);
		
		AccessoryEntity accessory1 = new AccessoryEntity();
		accessory1.setName("Sunroof Cover");
		accessory1.setDescription("Protects sunroof from dust");
		accessory1.setPrice(50.0);
		accessory1.setType("Exterior");
		accessory1.setVehicle(vehicle);
		accessoryRepository.save(accessory1);
		
		// Act & Assert
		mockMvc.perform(get("/api/accessory/getAccessoriesByVehicle/{vehicleId}?pageIndex=0&sizeOfPage=5", vehicle.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].name").value("Sunroof Cover"));
	}
	
	@Test
	void getAccessoriesByVehicle_ShouldReturnVehicleDoesNotExist() throws Exception {
		// Arrange
		Long nonExistantVehicleId = 999L;
		
		// Act & Assert
		mockMvc.perform(get("/api/accessory/getAccessoriesByVehicle/{vehicleId}?pageIndex=0&sizeOfPage=5", nonExistantVehicleId))
			.andExpect(status().isNotFound());
	}

}
