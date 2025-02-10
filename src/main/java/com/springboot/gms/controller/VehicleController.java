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

import com.springboot.gms.entities.Vehicle;
import com.springboot.gms.service.VehicleService;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

	private VehicleService vehicleService;

	public VehicleController(VehicleService vehicleService) {
		super();
		this.vehicleService = vehicleService;
	}

	@PostMapping("/add/{garageId}")
	public ResponseEntity<Vehicle> addVehicleToGarage(@PathVariable Long garageId, @RequestBody Vehicle vehicle) {
		Vehicle savedVehicle = vehicleService.addVehicleToGarage(garageId, vehicle);
		return new ResponseEntity<Vehicle>(savedVehicle, HttpStatus.CREATED);
	}

	@GetMapping("/getVehiclesByGarage/{garageId}")
	public Page<Vehicle> getVehiclesByGarage(@PathVariable Long garageId, @RequestParam int pageIndex,
			@RequestParam int sizeOfPage) {
		return vehicleService.getVehiclesByGarage(garageId, pageIndex, sizeOfPage);
	}

	@GetMapping("/getVehiclesByBrand")
	public Page<Vehicle> getVehiclesByBrand(@RequestParam String brand, @RequestParam int pageIndex,
			@RequestParam int sizeOfPage) {
		return vehicleService.getVehiclesByBrand(brand, pageIndex, sizeOfPage);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Vehicle> updateVehicle(@RequestBody Vehicle vehicle, @PathVariable Long id) {
		Vehicle updatedVehicle = vehicleService.updateVehicle(vehicle, id);
		return new ResponseEntity<Vehicle>(updatedVehicle, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteVehicle(@PathVariable Long id) {
		vehicleService.deleteVehicle(id);
		return new ResponseEntity<String>("Vehicle deleted successfully.", HttpStatus.OK);
	}

}
