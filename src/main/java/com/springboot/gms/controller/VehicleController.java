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

import com.springboot.gms.entities.VehicleEntity;
import com.springboot.gms.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

	private VehicleService vehicleService;

	public VehicleController(VehicleService vehicleService) {
		super();
		this.vehicleService = vehicleService;
	}

	@PostMapping("/addVehicleToGarage/{garageId}")
	public ResponseEntity<VehicleEntity> addVehicleToGarage(@PathVariable Long garageId, @RequestBody VehicleEntity vehicle) {
		VehicleEntity savedVehicle = vehicleService.addVehicleToGarage(garageId, vehicle);
		return new ResponseEntity<VehicleEntity>(savedVehicle, HttpStatus.CREATED);
	}

	@GetMapping("/getVehiclesByGarage/{garageId}")
	public Page<VehicleEntity> getVehiclesByGarage(@PathVariable Long garageId, @RequestParam int pageIndex,
			@RequestParam int sizeOfPage) {
		return vehicleService.getVehiclesByGarage(garageId, pageIndex, sizeOfPage);
	}

	@GetMapping("/getVehiclesByBrand")
	public Page<VehicleEntity> getVehiclesByBrand(@RequestParam String brand, @RequestParam int pageIndex,
			@RequestParam int sizeOfPage) {
		return vehicleService.getVehiclesByBrand(brand, pageIndex, sizeOfPage);
	}
	
	@PutMapping("/updateVehicle/{id}")
	public ResponseEntity<VehicleEntity> updateVehicle(@RequestBody VehicleEntity vehicle, @PathVariable Long id) {
		VehicleEntity updatedVehicle = vehicleService.updateVehicle(vehicle, id);
		return new ResponseEntity<VehicleEntity>(updatedVehicle, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteVehicle/{id}")
	public ResponseEntity<String> deleteVehicle(@PathVariable Long id) {
		vehicleService.deleteVehicle(id);
		return new ResponseEntity<String>("Vehicle deleted successfully.", HttpStatus.NO_CONTENT);
	}

}
