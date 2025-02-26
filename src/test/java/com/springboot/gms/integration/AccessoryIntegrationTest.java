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

import com.springboot.gms.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AccessoryIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void addAccessoryToVehicle_ShouldReturnCreatedAccessory() throws Exception {
		// Arrange
		Long vehicleId = 1L;

		String accessoryJson = """
					{
						"name": "Sunroof Cover",
						"description": "Protects sunroof from dust",
						"price": 50.0,
						"type": "Exterior"
					}
				""";

		// Act & Assert
		mockMvc.perform(post("/api/accessories/addAccessoryToVehicle/{vehicleId}", vehicleId)
				.contentType(MediaType.APPLICATION_JSON).content(accessoryJson)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Sunroof Cover"));
	}

	@Test
	void addAccessoryToNonExistantVehicle_ShouldReturnVehicleDoesNotExist() throws Exception {
		// Arrange
		Long nonExistantVehicleId = 999L;

		ResourceNotFoundException exc = new ResourceNotFoundException("Vehicle", "ID", nonExistantVehicleId);

		String accessoryJson = """
					{
						"name": "Sunroof Cover",
						"description": "Protects sunroof from dust",
						"price": 50.0,
						"type": "Exterior"
					}
				""";

		mockMvc.perform(post("/api/accessories/addAccessoryToVehicle/{vehicleId}", nonExistantVehicleId)
				.contentType(MediaType.APPLICATION_JSON).content(accessoryJson)).andExpect(status().isNotFound())
				.andExpect(content().string(exc.getMessage()));
	}
	
	@Test
	void addAccessoryToVehicle_invalidInput() throws Exception {
		// Arrange
		Long vehicleId = 1L;

		String accessoryJson = """
					{
						"description": "Protects sunroof from dust",
						"price": 50.0,
						"type": "Exterior"
					}
				""";

		mockMvc.perform(post("/api/accessories/addAccessoryToVehicle/{vehicleId}", vehicleId)
				.contentType(MediaType.APPLICATION_JSON).content(accessoryJson)).andExpect(status().isBadRequest())
				.andExpect(content().string("Name must not be null"));
	}

	@Test
	void updateAccessory_ShouldReturnUpdatedAccessory() throws Exception {
		// Arrange
		Long accessoryId = 1L;

		String accessoryJson = """
					{
						"name": "Sunroof Cover",
						"description": "Protects sunroof from dust",
						"price": 60.0,
						"type": "Exterior"
					}
				""";

		// Act & Assert
		mockMvc.perform(put("/api/accessories/updateAccessory/{accessoryId}", accessoryId)
				.contentType(MediaType.APPLICATION_JSON).content(accessoryJson)).andExpect(status().isOk())
				.andExpect(jsonPath("$.price").value(60.0));
	}

	@Test
	void updateAccessory_ShouldReturnAccessoryDoesNotExist() throws Exception {
		// Arrange
		Long nonExistantAccessoryId = 999L;

		ResourceNotFoundException exc = new ResourceNotFoundException("Accessory", "ID", nonExistantAccessoryId);

		String accessoryJson = """
					{
						"name": "Sunroof Cover",
						"description": "Protects sunroof from dust",
						"price": 60.0,
						"type": "Exterior"
					}
				""";

		// Act & Assert
		mockMvc.perform(put("/api/accessories/updateAccessory/{accessoryId}", nonExistantAccessoryId)
				.contentType(MediaType.APPLICATION_JSON).content(accessoryJson)).andExpect(status().isNotFound())
				.andExpect(content().string(exc.getMessage()));
	}
	
	@Test
	void updateAccessory_invalidInput() throws Exception {
		// Arrange
		Long accessoryId = 1L;

		String accessoryJson = """
					{
						"description": "Protects sunroof from dust",
						"price": 50.0,
						"type": "Exterior"
					}
				""";

		mockMvc.perform(put("/api/accessories/updateAccessory/{accessoryId}", accessoryId)
				.contentType(MediaType.APPLICATION_JSON).content(accessoryJson)).andExpect(status().isBadRequest())
				.andExpect(content().string("Name must not be null"));
	}

	@Test
	void deleteAccessory_ShouldDelete() throws Exception {
		// Arrange
		Long accessoryId = 1L;

		// Act & Assert
		mockMvc.perform(delete("/api/accessories/deleteAccessory/{accessoryId}", accessoryId))
				.andExpect(status().isNoContent()).andExpect(content().string("Accessory deleted successfully."));
	}

	@Test
	void deleteAccessory_ShouldReturnAccessoryDoesNotExist() throws Exception {
		// Arrange
		Long nonExistantAccessoryId = 999L;

		ResourceNotFoundException exc = new ResourceNotFoundException("Accessory", "ID", nonExistantAccessoryId);

		// Act & Assert
		mockMvc.perform(delete("/api/accessories/deleteAccessory/{accessoryId}", nonExistantAccessoryId))
				.andExpect(status().isNotFound()).andExpect(content().string(exc.getMessage()));
	}

	@Test
	void getAccessoriesByVehicle_ShouldReturnPageOfAccessories() throws Exception {
		// Arrange
		Long vehicleId = 1L;

		// Act & Assert
		mockMvc.perform(get("/api/accessories/getAccessoriesByVehicle/{vehicleId}?pageIndex=0&sizeOfPage=5", vehicleId))
				.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].name").value("Bluetooth Stereo System"));
	}

	@Test
	void getAccessoriesByVehicle_ShouldReturnVehicleDoesNotExist() throws Exception {
		// Arrange
		Long nonExistantVehicleId = 999L;

		ResourceNotFoundException exc = new ResourceNotFoundException("Vehicle", "ID", nonExistantVehicleId);

		// Act & Assert
		mockMvc.perform(get("/api/accessories/getAccessoriesByVehicle/{vehicleId}?pageIndex=0&sizeOfPage=5",
				nonExistantVehicleId)).andExpect(status().isNotFound()).andExpect(content().string(exc.getMessage()));
	}

}
