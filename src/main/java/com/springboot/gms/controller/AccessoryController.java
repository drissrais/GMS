package com.springboot.gms.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.gms.entities.Accessory;
import com.springboot.gms.service.AccessoryService;

@RestController
@RequestMapping("/api/accessory")
public class AccessoryController {

	private AccessoryService accessoryService;

	public AccessoryController(AccessoryService accessoryService) {
		super();
		this.accessoryService = accessoryService;
	}

	@PostMapping("/add/{vehicleId}")
	public ResponseEntity<Accessory> addAccessoryToVehicle(@RequestBody Accessory accessory,
			@PathVariable Long vehicleId) {
		Accessory savedAccessory = accessoryService.addAccessoryToVehicle(accessory, vehicleId);
		return new ResponseEntity<Accessory>(savedAccessory, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Accessory> updateAccessory(@RequestBody Accessory accessory, @PathVariable Long id) {
		Accessory updatedAccessory = accessoryService.updateAccessory(accessory, id);
		return new ResponseEntity<Accessory>(updatedAccessory, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteAccessory(@PathVariable Long id) {
		accessoryService.deleteAccessory(id);
		return new ResponseEntity<String>("Accessory deleted successfully.", HttpStatus.OK);
	}

	@GetMapping("/getAccessoriesByVehicle/{vehicleId}")
	public Page<Accessory> getAccessoriesByVehicle(@PathVariable Long vehicleId, @RequestParam int pageIndex,
			@RequestParam int sizeOfPage) {
		return accessoryService.getAccessoriesByVehicle(vehicleId, pageIndex, sizeOfPage);
	}

}
