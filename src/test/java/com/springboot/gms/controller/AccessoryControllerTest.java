package com.springboot.gms.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.gms.entities.AccessoryEntity;
import com.springboot.gms.exception.InvalidDataException;
import com.springboot.gms.exception.ResourceNotFoundException;
import com.springboot.gms.service.AccessoryService;
import com.springboot.gms.service.GarageService;
import com.springboot.gms.service.VehicleService;

@WebMvcTest(AccessoryController.class)
public class AccessoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccessoryService accessoryService;

	@MockBean
	private VehicleService vehicleService;

	@MockBean
	private GarageService garageService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void addAccessoryToVehicle_ShouldReturnAccessory() throws Exception {
		// Arrange
		Long vehicleId = 1L;

		AccessoryEntity accessory = AccessoryEntity.builder().id(1L).name("GPS Tracker").build();

		Mockito.when(accessoryService.addAccessoryToVehicle(Mockito.any(AccessoryEntity.class), Mockito.eq(vehicleId)))
				.thenReturn(accessory);

		// Act & Assert
		mockMvc.perform(post("/api/accessories/addAccessoryToVehicle/{vehicleId}", vehicleId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accessory)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.name").value("GPS Tracker"));
	}

	@Test
	void addAccessoryToVehicle_shouldThrowException_whenVehicleNotFound() throws Exception {
		// Arrange
		Long vehicleId = 999L;

		AccessoryEntity accessory = AccessoryEntity.builder().id(1L).build();

		Mockito.when(accessoryService.addAccessoryToVehicle(Mockito.any(AccessoryEntity.class), Mockito.eq(vehicleId)))
				.thenThrow(new ResourceNotFoundException("Vehicle", "ID", vehicleId));

		// Act & Assert
		mockMvc.perform(post("/api/accessories/addAccessoryToVehicle/{vehicleId}", vehicleId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accessory)))
				.andExpect(status().isNotFound()).andExpect(content().string("Vehicle not found with ID: '999'"));
	}

	@Test
	void addAccessoryToVehicle_invalidInput() throws Exception {
		// Arrange
		Long vehicleId = 1L;

		AccessoryEntity accessory = AccessoryEntity.builder().id(1L).build();

		InvalidDataException exc = new InvalidDataException("Accessory's name must be not null.");

		Mockito.when(accessoryService.addAccessoryToVehicle(Mockito.any(AccessoryEntity.class), Mockito.eq(vehicleId)))
				.thenThrow(exc);

		// Act & Assert
		mockMvc.perform(post("/api/accessories/addAccessoryToVehicle/{vehicleId}", vehicleId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accessory)))
				.andExpect(status().isBadRequest()).andExpect(content().string(exc.getMessage()));
	}

	@Test
	void updateAccessory_ShouldReturnAccessory() throws Exception {
		// Arrange
		Long accessoryId = 1L;

		AccessoryEntity accessory = AccessoryEntity.builder().id(accessoryId).name("GPS Tracker").build();

		Mockito.when(accessoryService.updateAccessory(Mockito.any(AccessoryEntity.class), Mockito.eq(accessoryId)))
				.thenReturn(accessory);

		// Act & Assert
		mockMvc.perform(put("/api/accessories/updateAccessory/{accessoryId}", accessoryId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(accessory)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.name").value("GPS Tracker"));
	}

	@Test
	void updateAccessory_shouldThrowException_whenAccessoryNotFound() throws Exception {
		// Arrange
		Long accessoryId = 999L;

		AccessoryEntity accessory = AccessoryEntity.builder().id(accessoryId).build();

		Mockito.when(accessoryService.updateAccessory(Mockito.any(AccessoryEntity.class), Mockito.eq(accessoryId)))
				.thenThrow(new ResourceNotFoundException("Accessory", "ID", accessoryId));

		// Act & Assert
		mockMvc.perform(put("/api/accessories/updateAccessory/{accessoryId}", accessoryId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accessory)))
				.andExpect(status().isNotFound()).andExpect(content().string("Accessory not found with ID: '999'"));
	}
	
	@Test
	void updateAccessory_invalidInput() throws Exception {
		// Arrange
		Long accessoryId = 1L;

		AccessoryEntity accessory = AccessoryEntity.builder().id(accessoryId).build();

		InvalidDataException exc = new InvalidDataException("Accessory's name must be not null.");

		Mockito.when(accessoryService.updateAccessory(Mockito.any(AccessoryEntity.class), Mockito.eq(accessoryId)))
				.thenThrow(exc);

		// Act & Assert
		mockMvc.perform(put("/api/accessories/updateAccessory/{accessoryId}", accessoryId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(accessory)))
				.andExpect(status().isBadRequest()).andExpect(content().string(exc.getMessage()));
	}

	@Test
	void deleteAccessory_ShouldDelete() throws Exception {
		// Arrange
		Long accessoryId = 1L;

		Mockito.doNothing().when(accessoryService).deleteAccessory(Mockito.eq(accessoryId));

		// Act & Assert
		mockMvc.perform(delete("/api/accessories/deleteAccessory/{accessoryId}", accessoryId))
				.andExpect(status().isNoContent()).andExpect(content().string("Accessory deleted successfully."));
	}

	@Test
	void deleteAccessory_shouldThrowException_whenAccessoryNotFound() throws Exception {
		// Arrange
		Long accessoryId = 999L;

		ResourceNotFoundException exc = new ResourceNotFoundException("Accessory", "ID", accessoryId);

		Mockito.doThrow(exc).when(accessoryService).deleteAccessory(Mockito.eq(accessoryId));

		mockMvc.perform(delete("/api/accessories/deleteAccessory/{accessoryId}", accessoryId))
				.andExpect(status().isNotFound()).andExpect(content().string(exc.getMessage()));
	}

	@Test
	void getAccessoriesByVehicle_ShouldReturnPageOfAccessories() throws Exception {
		// Arrange
		Long vehicleId = 1L;

		AccessoryEntity accessory1 = AccessoryEntity.builder().id(1L).name("GPS Tracker").build();

		Page<AccessoryEntity> accessoryPage = new PageImpl<>(List.of(accessory1));

		Mockito.when(
				accessoryService.getAccessoriesByVehicle(Mockito.eq(vehicleId), Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(accessoryPage);

		// Act & Assert
		mockMvc.perform(get("/api/accessories/getAccessoriesByVehicle/{vehicleId}?pageIndex=0&sizeOfPage=5", vehicleId))
				.andExpect(status().isOk()).andExpect(jsonPath("$.content.length()").value(1))
				.andExpect(jsonPath("$.content[0].name").value("GPS Tracker"));
	}

	@Test
	void getAccessoriesByVehicle_shouldThrowException_whenVehicleNotFound() throws Exception {
		// Arrange
		Long vehicleId = 999L;

		ResourceNotFoundException exc = new ResourceNotFoundException("Vehicle", "ID", vehicleId);

		Mockito.when(
				accessoryService.getAccessoriesByVehicle(Mockito.eq(vehicleId), Mockito.anyInt(), Mockito.anyInt()))
				.thenThrow(exc);

		// Act & Assert
		mockMvc.perform(get("/api/accessories/getAccessoriesByVehicle/{vehicleId}?pageIndex=0&sizeOfPage=5", vehicleId))
				.andExpect(status().isNotFound()).andExpect(content().string(exc.getMessage()));

	}

}
