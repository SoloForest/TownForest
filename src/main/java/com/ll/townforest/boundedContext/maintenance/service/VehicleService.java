package com.ll.townforest.boundedContext.maintenance.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.maintenance.entity.Vehicle;
import com.ll.townforest.boundedContext.maintenance.form.GuestVehicleDTO;
import com.ll.townforest.boundedContext.maintenance.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {
	private final VehicleRepository vehicleRepository;

	@Transactional
	public Vehicle create(String name, String vehicleNumber) {
		Vehicle vehicle = Vehicle.builder()
			.name(name)
			.vehicleNumber(vehicleNumber)
			.type(0)
			.build();
		return vehicleRepository.save(vehicle);
	}

	public List<Vehicle> findByUserId(Long id) {
		return vehicleRepository.findByUserId(id);
	}

	public RsData<AptAccount> canRegisterGuest(AptAccount user, String fullName) {
		if (!user.getAccount().getFullName().equals(fullName)) {
			return RsData.of("F-1", "신청자 이름이 올바르지 않습니다.");
		}

		if (!user.isStatus()) {
			return RsData.of("F-2", "해당 아파트 주민으로 승인받지 못했습니다.");
		}

		return RsData.of("S-1", "방문차량 등록이 가능한 계정입니다.", user);
	}

	@Transactional
	public RsData<Vehicle> createGuest(AptAccount user, GuestVehicleDTO guestVehicleDTO) {
		Vehicle vehicle = vehicleRepository.save(Vehicle.builder()
			.user(user)
			.vehicleNumber(guestVehicleDTO.getVehicleNumber())
			.name(guestVehicleDTO.getName())
			.type(1)
			.date(guestVehicleDTO.getSelectedDate())
			.build());

		return RsData.of("S-1", "방문 차량을 등록했습니다!", vehicle);
	}
}