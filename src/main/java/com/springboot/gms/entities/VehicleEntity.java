package com.springboot.gms.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicle")
public class VehicleEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "brand", nullable = false)
	private String brand;
	
	@Column(name = "manufacturing_year")
	private String manufacturingYear;
	
	@Column(name = "fuel_type")
	private String fuelType;
	
	@ManyToOne
	@JoinColumn(name = "garage_id", nullable = false)
	@JsonIgnore
	private GarageEntity garage;
	
	@OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AccessoryEntity> accessories;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getManufacturingYear() {
		return manufacturingYear;
	}

	public void setManufacturingYear(String manufacturingYear) {
		this.manufacturingYear = manufacturingYear;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public GarageEntity getGarage() {
		return garage;
	}

	public void setGarage(GarageEntity garage) {
		this.garage = garage;
	}
	
	public List<AccessoryEntity> getAccessories() {
		return accessories;
	}
	
	public void setAccessories(List<AccessoryEntity> accessories) {
		this.accessories = accessories;
	}

}
