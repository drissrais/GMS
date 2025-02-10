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
import com.springboot.gms.entities.Accessory;
import com.springboot.gms.service.AccessoryService;
import com.springboot.gms.service.GarageService;
import com.springboot.gms.service.VehicleService;

@WebMvcTest
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

		Accessory accessory = new Accessory();
		accessory.setId(1L);
		accessory.setName("GPS Tracker");

		Mockito.when(accessoryService.addAccessoryToVehicle(Mockito.any(Accessory.class), Mockito.eq(vehicleId)))
				.thenReturn(accessory);

		// Act & Assert
		mockMvc.perform(post("/api/accessory/add/{vehicleId}", vehicleId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(accessory))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("GPS Tracker"));
	}

	@Test
	void updateAccessory_ShouldReturnAccessory() throws Exception {
		// Arrange
		Long accessoryId = 1L;

		Accessory accessory = new Accessory();
		accessory.setId(accessoryId);
		accessory.setName("GPS Tracker");

		Mockito.when(accessoryService.updateAccessory(Mockito.any(Accessory.class), Mockito.eq(accessoryId)))
				.thenReturn(accessory);

		// Act & Assert
		mockMvc.perform(put("/api/accessory/update/{accessoryId}", accessoryId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(accessory))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.name").value("GPS Tracker"));
	}
	
	@Test
	void deleteAccessory_ShouldDelete() throws Exception {
		// Arrange
		Long accessoryId = 1L;
		
		Mockito.doNothing().when(accessoryService).deleteAccessory(Mockito.eq(accessoryId));
		
		// Act & Assert
		mockMvc.perform(delete("/api/accessory/delete/{accessoryId}", accessoryId))
			.andExpect(status().isOk())
			.andExpect(content().string("Accessory deleted successfully."));
	}

	@Test
	void getAccessoriesByVehicle_ShouldReturnPageOfAccessories() throws Exception {
		// Arrange
		Long vehicleId = 1L;

		Accessory accessory1 = new Accessory();
		accessory1.setId(1L);
		accessory1.setName("GPS Tracker");

		Page<Accessory> accessoryPage = new PageImpl<>(List.of(accessory1));

		Mockito.when(
				accessoryService.getAccessoriesByVehicle(Mockito.eq(vehicleId), Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(accessoryPage);

		// Act & Assert
		mockMvc.perform(get("/api/accessory/getAccessoriesByVehicle/{vehicleId}?pageIndex=0&sizeOfPage=5", vehicleId))
				.andExpect(status().isOk()).andExpect(jsonPath("$.content[0].name").value("GPS Tracker"));
	}

}
