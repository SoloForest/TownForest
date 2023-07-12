package com.ll.townforest.boundedContext.apt.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.House;
import com.ll.townforest.boundedContext.apt.entity.HouseHistory;
import com.ll.townforest.boundedContext.apt.repository.HouseHistoryRepository;
import com.ll.townforest.boundedContext.apt.repository.HouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HouseHistoryService {
	private final HouseHistoryRepository houseHistoryRepository;
	private final HouseRepository houseRepository;
	private final AptAccountService aptAccountService;

	public List<House> canBookingHousesWithDate(LocalDateTime selectedDate) {
		return houseHistoryRepository.findCanBookingHousesWithDate(selectedDate);
	}

	public Slice<HouseHistory> historiesByUserWithPage(int page, Long userId) {
		return houseHistoryRepository.findByUserIdOrderByIdDesc(userId, PageRequest.of(page, 5));
	}

	public RsData<AptAccount> canBooking(AptAccount aptAccount, LocalDateTime selectedDate) {
		if (!aptAccount.isStatus()) {
			return RsData.of("F-1", "해당 아파트에 권한이 없습니다.");
		}

		LocalDateTime startOfDay = selectedDate.toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
		List<HouseHistory> houseHistory = houseHistoryRepository
			.findByUserIdAndSelectedDateBetween(aptAccount.getId(), startOfDay, endOfDay);
		if (houseHistory.size() > 0) {
			return RsData.of("F-2", "동일한 날짜에 게스트하우스<br>신청 이력이 존재합니다.");
		}

		return RsData.of("S-1", "게스트하우스 예약 가능한 이용자입니다.", aptAccount);
	}

	public RsData<House> canBooking(Long houseId, LocalDateTime selectedDate) {
		Optional<House> target = houseRepository.findById(houseId);
		if (target.isEmpty()) {
			return RsData.of("F-1", "존재하지 않는 호실입니다.");
		}

		LocalDateTime startOfDay = selectedDate.toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
		List<HouseHistory> houseHistory = houseHistoryRepository
			.findByHouseIdAndStatusAndSelectedDateBetween(houseId, 1, startOfDay, endOfDay);
		if (houseHistory.size() > 0) {
			return RsData.of("F-2", "이미 예약된 호실입니다.");
		}

		return RsData.of("S-1", "예약 가능한 호실입니다.", target.get());
	}

	@Transactional
	public RsData<HouseHistory> booking(LocalDateTime selectedDate, AptAccount user, House house) {
		HouseHistory houseHistory = HouseHistory.builder()
			.house(house)
			.houseAddress(house.getAddress())
			.user(user)
			.fullName(user.getAccount().getFullName())
			.userAddress(aptAccountService.makeAddressToString(user).getData())
			.selectedDate(selectedDate)
			.status(0)
			.build();
		houseHistoryRepository.save(houseHistory);
		return RsData.of("S-1",
			"%s, %s<br>게스트하우스 이용이<br>신청되었습니다.<br>관리자 승인 후 사용할<br>수 있습니다."
				.formatted(selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), house.getAddress()),
			houseHistory);
	}
}
