package com.springboot.gms.entities;

import java.time.DayOfWeek;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "daily_opening_time")
public class DailyOpeningTimeEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @OneToMany(mappedBy = "dailyOpeningTime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpeningTimeEntity> openingTimes;
    
    @ManyToOne
    @JoinColumn(name = "garage_id")
    @JsonIgnore
    private GarageEntity garage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public List<OpeningTimeEntity> getOpeningTimes() {
		return openingTimes;
	}

	public void setOpeningTimes(List<OpeningTimeEntity> openingTimes) {
		this.openingTimes = openingTimes;
	}
	
	public GarageEntity getGarage() {
		return garage;
	}
	
	public void setGarage(GarageEntity garage) {
		this.garage = garage;
	}

}
