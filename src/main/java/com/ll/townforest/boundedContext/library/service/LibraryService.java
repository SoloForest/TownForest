package com.ll.townforest.boundedContext.library.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
}
