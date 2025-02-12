package com.springboot.gms.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "garage")
public class GarageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "address")
	private String address;
	
	@Column(name = "city")
	private String city;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "email")
	private String email;

	@OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DailyOpeningTimeEntity> dailyOpeningTimeList;
	
	@OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VehicleEntity> vehicles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<DailyOpeningTimeEntity> getDailyOpeningTimeList() {
		return dailyOpeningTimeList;
	}
	
	public void setDailyOpeningTimeList(List<DailyOpeningTimeEntity> dailyOpeningTimeList) {
		this.dailyOpeningTimeList = dailyOpeningTimeList;
	}
	
	public List<VehicleEntity> getVehicles() {
		return vehicles;
	}
	
	public void setVehicles(List<VehicleEntity> vehicles) {
		this.vehicles = vehicles;
	}

}
