package com.ll.townforest.boundedContext.library.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.repository.AptAccountHouseRepository;
import com.ll.townforest.boundedContext.apt.repository.AptAccountRepository;
import com.ll.townforest.boundedContext.apt.repository.AptRepository;
import com.ll.townforest.boundedContext.library.entity.Library;
import com.ll.townforest.boundedContext.library.entity.LibraryHistory;
import com.ll.townforest.boundedContext.library.entity.Seat;
import com.ll.townforest.boundedContext.library.repository.LibraryHistoryRepository;
import com.ll.townforest.boundedContext.library.repository.LibraryRepository;
import com.ll.townforest.boundedContext.library.repository.SeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class LibraryService {
	private final LibraryRepository libraryRepository;
	private final SeatRepository seatRepository;
	private final LibraryHistoryRepository libraryHistoryRepository;
	private final AptAccountRepository aptAccountRepository;
	private final AptRepository aptRepository;
	private final AptAccountHouseRepository aptAccountHouseRepository;

	public List<Seat> findUseableList(Long libraryId) {
		Optional<Library> optLibrary = libraryRepository.findById(libraryId);
		if (optLibrary.isEmpty())
			return new ArrayList<Seat>();
		return seatRepository.findByLibraryIdAndStatus(libraryId, 0);
	}

	private Optional<LibraryHistory> lastUsingOfDay(Long aptAccountId) {
		LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

		return libraryHistoryRepository
			.findTopByUserIdAndDateBetweenOrderByDateDesc(aptAccountId, startOfDay, endOfDay);
	}

	private Optional<LibraryHistory> lastHistory(Long aptAccountId) {
		return libraryHistoryRepository.findTopByUserIdOrderByIdDesc(aptAccountId);
	}

	public LibraryHistory usingHistory(Long aptAccountId) {
		Optional<LibraryHistory> optLibraryHistory = lastUsingOfDay(aptAccountId);

		if (optLibraryHistory.isEmpty() || !optLibraryHistory.get().getStatusType().equals(0)) {
			return null;
		}
		return optLibraryHistory.get();
	}

	public RsData<AptAccount> canBooking(Long aptAccountId) {
		Optional<AptAccount> optAptAccount = aptAccountRepository.findById(aptAccountId);
		if (optAptAccount.isEmpty()
			|| !optAptAccount.get().getApt().getId().equals(1L)
			|| !optAptAccount.get().isStatus()
		) {
			return RsData.of("F-1", "해당 아파트 독서실 이용권한이 없습니다.", null);
		}

		Optional<LibraryHistory> optLibraryHistory = lastUsingOfDay(aptAccountId);
		if (optLibraryHistory.isPresent() && optLibraryHistory.get().getStatusType().equals(0)) {
			return RsData.of("F-2", "이미 이용중인 독서실 자리가 있습니다.", null);
		}

		return RsData.of("S-1", "해당 아파트 독서실 자리 예약에 문제 없는 계정입니다.", optAptAccount.get());
	}

	public RsData<Seat> canBooking(int seatNumber) {
		Optional<Seat> optSeat = seatRepository.findBySeatNumber(seatNumber);
		if (optSeat.isEmpty() || optSeat.get().getStatus() != 0) {
			return RsData.of("F-1", "사용 불가능한 자리입니다.", null);
		}
		return RsData.of("S-1", "예약에 문제 없는 자리입니다.", optSeat.get());
	}

	@Transactional
	public RsData<String> booking(AptAccount user, Seat seat, int selectedSeat) {
		RsData<String> addressString = makeAddressToString(user);
		if (addressString.isFail()) {
			return addressString;
		}

		libraryHistoryRepository.save(LibraryHistory.builder()
			.apart(aptRepository.findById(1L).orElse(null))
			.library(libraryRepository.findById(1L).orElse(null))
			.user(user)
			.addressToString(addressString.getData())
			.seat(seat)
			.date(LocalDateTime.now())
			.statusType(0)
			.build());

		seatRepository.save(seat.toBuilder()
			.status(1)
			.build());

		return RsData.of("S-1", "%03d번 자리를 예약했습니다.".formatted(selectedSeat));
	}

	private RsData<String> makeAddressToString(AptAccount user) {

		AptAccountHouse aptAccountHouse = aptAccountHouseRepository.findByUserId(user.getId()).orElse(null);
		if (aptAccountHouse == null) {
			return RsData.of("F-3", "주소를 알 수 없는 계정입니다.");
		}

		return RsData.of("S-1", "주소를 문자열로 변환합니다.",
			aptAccountHouse.getHouse().getDong() + "동 " + aptAccountHouse.getHouse().getHo() + "호");
	}

	public RsData<AptAccount> canCancel(Long aptAccountId) {
		Optional<AptAccount> optAptAccount = aptAccountRepository.findById(aptAccountId);
		if (optAptAccount.isEmpty()
			|| !optAptAccount.get().getApt().getId().equals(1L)
			|| !optAptAccount.get().isStatus()
		) {
			return RsData.of("F-1", "해당 아파트 독서실 이용권한이 없습니다.", null);
		}

		Optional<LibraryHistory> optLibraryHistory = lastUsingOfDay(aptAccountId);
		if (optLibraryHistory.isEmpty() || !optLibraryHistory.get().getStatusType().equals(0)) {
			return RsData.of("F-2", "이용중인 독서실 자리가 없습니다.", null);
		}
		if (!optLibraryHistory.get().getUser().equals(optAptAccount.get())) {
			return RsData.of("F-3", "이용중인 자리가 아닙니다.", null);
		}

		return RsData.of("S-1", "해당 아파트 독서실 자리 예약 취소에 문제 없는 계정입니다.", optAptAccount.get());
	}

	public RsData<Seat> canCancel(int seatNumber) {
		Optional<Seat> optSeat = seatRepository.findBySeatNumber(seatNumber);
		if (optSeat.isEmpty() || optSeat.get().getStatus() != 1) {
			return RsData.of("F-1", "비어있는 자리입니다.", null);
		}

		return RsData.of("S-1", "예약 취소 가능한 자리입니다.", optSeat.get());
	}

	@Transactional
	public RsData<String> cancel(AptAccount user, Seat seat, int selectedSeat) {
		RsData<String> addressString = makeAddressToString(user);
		if (addressString.isFail()) {
			return addressString;
		}

		libraryHistoryRepository.save(LibraryHistory.builder()
			.apart(aptRepository.findById(1L).orElse(null))
			.library(libraryRepository.findById(1L).orElse(null))
			.user(user)
			.addressToString(addressString.getData())
			.seat(seat)
			.date(LocalDateTime.now())
			.statusType(1)
			.build());

		seatRepository.save(seat.toBuilder()
			.status(0)
			.build());

		return RsData.of("S-1", "%03d번 자리 이용을 취소합니다.".formatted(selectedSeat));
	}

	public Slice<LibraryHistory> findHistories(Long aptAccountId, Pageable pageable) {
		return libraryHistoryRepository.findByUserIdOrderByIdDesc(aptAccountId, pageable);
	}

	public Slice<LibraryHistory> findAllHistories(Pageable pageable) {
		return libraryHistoryRepository.findAllByOrderByIdDesc(pageable);
	}

	public RsData<AptAccount> canAdminCancel(AptAccount user) {
		if (user.getApt().getId() != 1) {
			return RsData.of("F-1", "해당 아파트에 대한 권한이 없습니다.");
		}

		if (user.getAuthority() != 2) {
			return RsData.of("F-2", "독서실 관리자만 가능한 동작입니다.");
		}

		return RsData.of("S-1", "취소 가능한 계정입니다.", user);
	}

	public RsData<Seat> canAdminCancel(Long libraryHistoryId) {
		Optional<LibraryHistory> optLibraryHistory = libraryHistoryRepository.findById(libraryHistoryId);
		if (optLibraryHistory.isEmpty()) {
			return RsData.of("F-1", "존재하지 않는 히스토리입니다.");
		}
		LibraryHistory libraryHistory = optLibraryHistory.get();

		Optional<Seat> optSeat = seatRepository.findBySeatNumber(libraryHistory.getSeat().getSeatNumber());
		if (optSeat.isEmpty() || optSeat.get().getStatus() != 1) {
			return RsData.of("F-1", "비어있는 자리입니다.");
		}

		Optional<LibraryHistory> lastHistoryOfTarget = lastHistory(libraryHistory.getUser().getId());
		if (lastHistoryOfTarget.isEmpty()) {
			return RsData.of("F-2", "대상자 이용 내역이 존재하지 않습니다.");
		}
		if (!lastHistoryOfTarget.get().equals(libraryHistory)) {
			return RsData.of("F-3", "대상자의 마지막 이력이 아닙니다.");
		}

		return RsData.of("S-1", "취소 가능한 자리입니다.", optSeat.get());
	}

	public AptAccount getTargetUserForAdminCancel(Long libraryHistoryId) {
		return libraryHistoryRepository.findById(libraryHistoryId).get().getUser();
	}

	@Transactional
	public RsData<String> adminCancel(AptAccount targetUser, Seat seat, Long libraryHistoryId) {
		RsData<String> addressString = makeAddressToString(targetUser);
		if (addressString.isFail()) {
			return addressString;
		}

		libraryHistoryRepository.save(LibraryHistory.builder()
			.apart(aptRepository.findById(1L).orElse(null))
			.library(libraryRepository.findById(1L).orElse(null))
			.user(targetUser)
			.addressToString(addressString.getData())
			.seat(seat)
			.date(LocalDateTime.now())
			.statusType(3)
			.build());

		seatRepository.save(seat.toBuilder()
			.status(0)
			.build());

		return RsData.of("S-1",
			"%s님의 %03d번 자리 이용을 취소합니다.".formatted(targetUser.getAccount().getFullName(), seat.getSeatNumber()));
	}
}
