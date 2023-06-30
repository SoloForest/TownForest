package com.ll.townforest.boundedContext.library.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
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

		return RsData.of("S-1", "해당 아파트 독서실 이용에 문제 없는 계정입니다.", optAptAccount.get());
	}

	public RsData<Seat> canBooking(int seatNumber) {
		Optional<Seat> optSeat = seatRepository.findBySeatNumber(seatNumber);
		if (optSeat.isEmpty() || optSeat.get().getStatus() != 0) {
			return RsData.of("F-1", "사용 불가능한 자리입니다.", null);
		}
		return RsData.of("S-1", "예약에 문제 없는 자리입니다.", optSeat.get());
	}

	public RsData<String> booking(AptAccount user, Seat seat, int selectedSeat) {
		libraryHistoryRepository.save(LibraryHistory.builder()
			.apart(aptRepository.findById(1L).orElse(null))
			.library(libraryRepository.findById(1L).orElse(null))
			.user(user)
			.seat(seat)
			.date(LocalDateTime.now())
			.statusType(0)
			.build());

		seatRepository.save(seat.toBuilder()
			.status(1)
			.build());

		return RsData.of("S-1", "%03d번 자리를 예약했습니다.".formatted(selectedSeat));
	}
}
