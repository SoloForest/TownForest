package com.ll.townforest.boundedContext.maintenance.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.repository.AptAccountHouseRepository;
import com.ll.townforest.boundedContext.apt.service.AptAccountService;
import com.ll.townforest.boundedContext.maintenance.entity.Vehicle;
import com.ll.townforest.boundedContext.maintenance.form.GuestVehicleDTO;
import com.ll.townforest.boundedContext.maintenance.form.GuestVehicleHistory;
import com.ll.townforest.boundedContext.maintenance.form.VehicleForm;
import com.ll.townforest.boundedContext.maintenance.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {
	private final VehicleRepository vehicleRepository;

	private final AptAccountHouseRepository aptAccountHouseRepository;

	private final AptAccountService aptAccountService;

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

	public String getUserId(Long userId) {
		AptAccountHouse aptAccountHouse = findByUserId(userId);
		if (aptAccountHouse == null) {
			return null;
		}
		Long houseId = aptAccountHouse.getHouse().getId();
		return String.format("redirect:/maintenance/vehicle/%d", houseId);
	}

	public VehicleResult delete(Long vehicleId, Long userId) {
		Optional<AptAccountHouse> ownedAptAccountHouse = aptAccountHouseRepository.findByUserId(userId);
		if (ownedAptAccountHouse.isPresent()) {
			Long houseId = ownedAptAccountHouse.get().getHouse().getId();
			Optional<Vehicle> vehicleOptional = findByVehicleId(vehicleId);

			if (vehicleOptional.isPresent()) {
				Vehicle vehicle = vehicleOptional.get();
				if (vehicle.getAptHouse().getId().equals(houseId)) {
					vehicleRepository.deleteById(vehicleId);
					return VehicleResult.SUCCESS;
				}
			}
		}
		return VehicleResult.FAILED;
	}

	public VehicleResult getAllId(Long userId, VehicleForm form) {
		Optional<AptAccountHouse> ownedAptAccountHouse = aptAccountHouseRepository.findByUserId(userId);
		if (ownedAptAccountHouse.isPresent()) {
			Long houseId = ownedAptAccountHouse.get().getHouse().getId();

			List<AptAccountHouse> allMembersOfHouse = aptAccountHouseRepository.findAllByHouseIdAndStatusFalse(houseId);

			if (!getHouseMembers(form.getName(), allMembersOfHouse)) {
				return VehicleResult.NAME_INVALID;
			}

			if (getVehicleNumber(form.getVehicleNumber(), houseId)) {
				return VehicleResult.VEHICLE_DUPLICATION;
			}

			create(form);
			return VehicleResult.SUCCESS;
		}
		return VehicleResult.FAILED;
	}

	private boolean getHouseMembers(String inputFullName, List<AptAccountHouse> allMembersOfHouse) {
		for (AptAccountHouse member : allMembersOfHouse) {
			if (member.getUser() != null && member.getUser().getAccount() != null) {
				if (inputFullName != null && inputFullName.equals(member.getUser().getAccount().getFullName())) {
					return true;
				}
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

	public Slice<Vehicle> findGuestHistories(Long aptAccountId, Pageable pageable) {
		return vehicleRepository.findByUserIdAndTypeOrderByIdDesc(aptAccountId, 1, pageable);
	}

	public Page<Vehicle> findBeforeGuest(LocalDateTime endDate, int page) {
		return vehicleRepository.findByTypeAndDateBefore(1, endDate, PageRequest.of(page, 25));
	}

	public Page<Vehicle> findTodayGuest(LocalDateTime startOfDay, LocalDateTime endOfDay, int page) {
		return vehicleRepository.findByTypeAndDateBetween(1, startOfDay, endOfDay, PageRequest.of(page, 25));
	}

	public Page<Vehicle> findAfterGuest(LocalDateTime startDate, int page) {
		return vehicleRepository.findByTypeAndDateGreaterThanEqual(1, startDate, PageRequest.of(page, 25));
	}

	public Page<GuestVehicleHistory> findGuestByTab(int page, int tab) {
		LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
		Page<Vehicle> vehicles;

		switch (tab) {
			case 0 -> vehicles = findBeforeGuest(today.minusSeconds(1), page);
			case 2 -> vehicles = findAfterGuest(today.plusDays(1), page);
			default -> vehicles = findTodayGuest(today, today.plusDays(1).minusSeconds(1), page);
		}

		List<GuestVehicleHistory> guestVehicleHistories = new ArrayList<>();

		for (Vehicle vehicle : vehicles) {
			GuestVehicleHistory history = new GuestVehicleHistory(vehicle);
			RsData<String> addressResult = aptAccountService.makeAddressToString(vehicle.getUser());
			if (addressResult.isFail()) {
				history.setAddress("주소를 찾을 수 없음");
			}
			history.setAddress(addressResult.getData());
			guestVehicleHistories.add(history);
		}

		return new PageImpl<>(guestVehicleHistories, PageRequest.of(page, 25), vehicles.getTotalElements());
	}
}