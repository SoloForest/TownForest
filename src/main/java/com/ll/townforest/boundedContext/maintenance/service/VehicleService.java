package com.ll.townforest.boundedContext.maintenance.service;

import java.time.LocalDateTime;
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
			.date(LocalDateTime.now())
			.user(form.getUser())
			.aptHouse(form.getAptHouse())
			.build();
		return vehicleRepository.save(vehicle);
	}

	public List<Vehicle> findByHouseId(Long houseId) { //어느 세대에 속하는지 조회
		return vehicleRepository.findByAptHouse_House_Id(houseId);
	}

	public AptAccountHouse findByUserId(Long userId) {
		Optional<AptAccountHouse> ownedAptAccountHouse = aptAccountHouseRepository.findByUserId(userId);

		if (ownedAptAccountHouse.isPresent()) {
			return ownedAptAccountHouse.get();
		}
		return null;
	}

	public List<Vehicle> getVehicleList(Long userId, Long houseId) {
		Optional<AptAccountHouse> ownedAptAccountHouse = aptAccountHouseRepository.findByUserId(userId);

		if (ownedAptAccountHouse.isPresent() && houseId.equals(ownedAptAccountHouse.get().getHouse().getId())) {
			return findByHouseId(houseId);
		} else {
			return null;
		}
	}

	public Optional<Vehicle> findByVehicleId(Long id) {
		return vehicleRepository.findById(id);
	}

	public String getUserid(Long userId) {
		AptAccountHouse aptAccountHouse = findByUserId(userId);
		if (aptAccountHouse == null) {
			return null;
		}
		Long houseId = aptAccountHouse.getHouse().getId();
		return String.format("redirect:/maintenance/vehicle/%d", houseId);
	}

	public VehicleResult delete(Long vehicleId, Long userId) {
		Optional<Vehicle> vehicleOptional = findByVehicleId(vehicleId);
		if (vehicleOptional.isPresent()) {
			Vehicle vehicle = vehicleOptional.get();
			if (vehicle.getUser().getId().equals(userId)) {
				vehicleRepository.deleteById(vehicleId);
				return VehicleResult.SUCCESS;
			}
		}
		return VehicleResult.FAILED;
	}

	public VehicleResult getAllId(Long userId, VehicleForm form) {
		Optional<AptAccountHouse> ownedAptAccountHouse = aptAccountHouseRepository.findByUserId(userId);
		if (ownedAptAccountHouse.isPresent()) {
			List<AptAccountHouse> allMembersOfHouse = aptAccountHouseRepository.findAllByHouseId(
				ownedAptAccountHouse.get().getHouse().getId());

			if (!getHouseMembers(form.getName(), allMembersOfHouse)) {
				return VehicleResult.NAME_INVALID;
			}

			if (getVehicleNumber(form.getVehicleNumber(), ownedAptAccountHouse.get().getHouse().getId())) {
				return VehicleResult.VEHICLE_DUPLICATION;
			}

			create(form);
			return VehicleResult.SUCCESS;
		}
		return VehicleResult.FAILED;
	}

	private boolean getHouseMembers(String inputFullName, List<AptAccountHouse> allMembersOfHouse) {
		for (AptAccountHouse member : allMembersOfHouse) {
			if (inputFullName != null && inputFullName.equals(member.getUser().getAccount().getFullName())) {
				return true;
			}
		}
		return false;
	}

	private boolean getVehicleNumber(String inputVehiclePlateNumber, Long houseId) {
		List<Vehicle> memberVehicles = vehicleRepository.findByAptHouse_House_Id(houseId);
		for (Vehicle vehicle : memberVehicles) {
			if (inputVehiclePlateNumber.equals(vehicle.getVehicleNumber())) {
				return true;
			}
		}
		return false;
	}

	public enum VehicleResult {
		SUCCESS,
		NAME_INVALID,
		VEHICLE_DUPLICATION,
		FAILED
	}
}