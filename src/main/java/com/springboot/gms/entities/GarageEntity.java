package com.springboot.gms.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "garage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String name;

	private String address;
	
	private String city;

	private String phoneNumber;

	private String email;

	@OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DailyOpeningTimeEntity> dailyOpeningTimeList;
	
	@OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VehicleEntity> vehicles;

}
