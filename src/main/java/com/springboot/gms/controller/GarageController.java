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

import com.springboot.gms.entities.Garage;
import com.springboot.gms.service.GarageService;

@RestController
@RequestMapping("/api/garage")
public class GarageController {

	private GarageService garageService;

	public GarageController(GarageService garageService) {
		super();
		this.garageService = garageService;
	}

	@PostMapping("/add")
	public ResponseEntity<Garage> saveGarage(@RequestBody Garage garage) {
		return new ResponseEntity<Garage>(garageService.saveGarage(garage), HttpStatus.CREATED);
	}

	@GetMapping("/get")
	public List<Garage> getAllGarages() {
		return garageService.getAllGarages();
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Garage> getGarageById(@PathVariable("id") long garageId) {
		return new ResponseEntity<Garage>(garageService.getGarageById(garageId), HttpStatus.OK);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Garage> updateGarage(@RequestBody Garage garage, @PathVariable Long id) {
		return new ResponseEntity<Garage>(garageService.updateGarage(garage, id), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteGarage(@PathVariable Long id) {
		garageService.deleteGarage(id);
		return new ResponseEntity<String>("Garage deleted successfully!", HttpStatus.OK);
	}

	@GetMapping("/getGarageListByCriteria")
	public Page<Garage> getGarageListByCriteria(@RequestParam(required = false) String name,
			@RequestParam(required = false) String city, @RequestParam int pageIndex, @RequestParam int sizeOfPage) {
		return garageService.getGarageListByCriteria(name, city, pageIndex, sizeOfPage);
	}

}
