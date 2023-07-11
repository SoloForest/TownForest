package com.ll.townforest.boundedContext.maintenance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.repository.AptAccountHouseRepository;
import com.ll.townforest.boundedContext.maintenance.entity.Vehicle;
import com.ll.townforest.boundedContext.maintenance.form.VehicleForm;
import com.ll.townforest.boundedContext.maintenance.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {
	private final VehicleRepository vehicleRepository;
	private final AptAccountHouseRepository aptAccountHouseRepository;

	@Transactional
	public Vehicle create(VehicleForm form) {
		Vehicle vehicle = Vehicle.builder()
			.name(form.getName())
			.vehicleNumber(form.getVehicleNumber())
			.type(0)
			.user(form.getUser())
			.aptHouse(form.getAptHouse())
			.build();
		return vehicleRepository.save(vehicle);
	}

	public List<Vehicle> findByHouseId(Long houseId) { //어느 세대에 속하는지 조회
		return vehicleRepository.findByAptHouse_House_Id(houseId);
	}

	public AptAccountHouse findByUserId(Long id) { //유저id 조회
		Optional<AptAccountHouse> aptAccountHouseOptional = aptAccountHouseRepository.findByUserId(id);
		return aptAccountHouseOptional.orElse(null);
	}

	public boolean accessTokenVehicle(Long userId, Long houseId) {
		AptAccountHouse ownedAptAccountHouse = findByUserId(userId);
		return ownedAptAccountHouse != null && houseId.equals(ownedAptAccountHouse.getHouse().getId());
	}

	public void deleteVehicleById(Long id) {
		vehicleRepository.deleteById(id);
	}

	public Optional<Vehicle> findByVehicleId(Long id) {
		return vehicleRepository.findById(id);
	}
}