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

import com.springboot.gms.entities.AccessoryEntity;
import com.springboot.gms.service.AccessoryService;

@RestController
@RequestMapping("/api/accessories")
public class AccessoryController {

	private AccessoryService accessoryService;

	public AccessoryController(AccessoryService accessoryService) {
		super();
		this.accessoryService = accessoryService;
	}

	@PostMapping("/addAccessoryToVehicle/{vehicleId}")
	public ResponseEntity<AccessoryEntity> addAccessoryToVehicle(@RequestBody AccessoryEntity accessory,
			@PathVariable Long vehicleId) {
		AccessoryEntity savedAccessory = accessoryService.addAccessoryToVehicle(accessory, vehicleId);
		return new ResponseEntity<AccessoryEntity>(savedAccessory, HttpStatus.CREATED);
	}

	@PutMapping("/updateAccessory/{id}")
	public ResponseEntity<AccessoryEntity> updateAccessory(@RequestBody AccessoryEntity accessory, @PathVariable Long id) {
		AccessoryEntity updatedAccessory = accessoryService.updateAccessory(accessory, id);
		return new ResponseEntity<AccessoryEntity>(updatedAccessory, HttpStatus.OK);
	}

	@DeleteMapping("/deleteAccessory/{id}")
	public ResponseEntity<String> deleteAccessory(@PathVariable Long id) {
		accessoryService.deleteAccessory(id);
		return new ResponseEntity<String>("Accessory deleted successfully.", HttpStatus.NO_CONTENT);
	}

	@GetMapping("/getAccessoriesByVehicle/{vehicleId}")
	public Page<AccessoryEntity> getAccessoriesByVehicle(@PathVariable Long vehicleId, @RequestParam int pageIndex,
			@RequestParam int sizeOfPage) {
		return accessoryService.getAccessoriesByVehicle(vehicleId, pageIndex, sizeOfPage);
	}

}
