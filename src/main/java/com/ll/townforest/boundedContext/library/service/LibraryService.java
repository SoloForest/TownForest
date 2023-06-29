package com.ll.townforest.boundedContext.library.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

	public Optional<LibraryHistory> lastUsingOfDay(Long aptAccountId) {
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

	public AptAccount canBooking(Long aptAccountId) {
		Optional<AptAccount> optAptAccount = aptAccountRepository.findById(aptAccountId);
		if (optAptAccount.isEmpty()
			|| !optAptAccount.get().getApt().getId().equals(1L)
			|| !optAptAccount.get().isStatus()
		) {
			return null; // 해당 아파트 독서실 이용권한이 없습니다.
		}
		return optAptAccount.get();
	}

	public Seat canBooking(int seatNumber) {
		Optional<Seat> optSeat = seatRepository.findBySeatNumber(seatNumber);
		if (optSeat.isEmpty() || optSeat.get().getStatus() != 0) {
			return null; // 사용 불가능한 자리입니다.
		}
		return optSeat.get();
	}

	public String booking(AptAccount user, Seat seat, int selectedSeat) {
		libraryHistoryRepository.save(LibraryHistory.builder()
			.apart(aptRepository.findById(1L).orElse(null))
			.library(libraryRepository.findById(1L).orElse(null))
			.user(user)
			.seat(seat)
			.date(LocalDateTime.now())
			.statusType(0)
			.build());

		return "%03d번 좌석을 예약했습니다.".formatted(selectedSeat);
	}
}
