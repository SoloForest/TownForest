package com.ll.townforest.boundedContext.maintenance.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.boundedContext.maintenance.entity.Vehicle;
import com.ll.townforest.boundedContext.maintenance.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {
	private final VehicleRepository vehicleRepository;

	@Transactional
	public Vehicle insert(String name, String vehicleNumber) {
		Vehicle vehicle = Vehicle.builder()
			.name(name)
			.vehicleNumber(vehicleNumber)
			.type(0)
			.build();
		return vehicleRepository.save(vehicle);
	}
}
