package com.springboot.gms.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.springboot.gms.entities.Garage;

import jakarta.persistence.criteria.Predicate;

public class GarageSpecification {

	public static Specification<Garage> findByCriteria(String name, String city) {
		
		return (root, query, criteriaBuilder) -> {
			
			List<Predicate> predicates = new ArrayList<>();
			
			if (name != null && !name.isEmpty()) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
			}
			
			if (city != null && !city.isEmpty()) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
			}
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			
		};
		
	}

}
