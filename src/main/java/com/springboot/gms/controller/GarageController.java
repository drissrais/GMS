package com.springboot.gms.controller;

import java.util.List;

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

import com.springboot.gms.entities.GarageEntity;
import com.springboot.gms.service.GarageService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RestController
@RequestMapping("/api/garages")
public class GarageController {

	private GarageService garageService;

	@PostMapping("/saveGarage")
	public ResponseEntity<GarageEntity> saveGarage(@RequestBody GarageEntity garage) {
		return new ResponseEntity<GarageEntity>(garageService.saveGarage(garage), HttpStatus.CREATED);
	}

	@GetMapping("/getAllGarages")
	public List<GarageEntity> getAllGarages() {
		return garageService.getAllGarages();
	}

	@GetMapping("/getGarageById/{id}")
	public ResponseEntity<GarageEntity> getGarageById(@PathVariable("id") long garageId) {
		return new ResponseEntity<GarageEntity>(garageService.getGarageById(garageId), HttpStatus.OK);
	}

	@PutMapping("/updateGarage/{id}")
	public ResponseEntity<GarageEntity> updateGarage(@RequestBody GarageEntity garage, @PathVariable Long id) {
		return new ResponseEntity<GarageEntity>(garageService.updateGarage(garage, id), HttpStatus.OK);
	}

	@DeleteMapping("/deleteGarage/{id}")
	public ResponseEntity<String> deleteGarage(@PathVariable Long id) {
		garageService.deleteGarage(id);
		return new ResponseEntity<String>("Garage deleted successfully!", HttpStatus.NO_CONTENT);
	}

	@GetMapping("/getGarageListByCriteria")
	public ResponseEntity<Page<GarageEntity>> getGarageListByCriteria(@RequestParam(required = false) String name,
			@RequestParam(required = false) String city, @RequestParam int pageIndex, @RequestParam int sizeOfPage) {
		return new ResponseEntity<Page<GarageEntity>>(
				garageService.getGarageListByCriteria(name, city, pageIndex, sizeOfPage), HttpStatus.OK);
	}

}
