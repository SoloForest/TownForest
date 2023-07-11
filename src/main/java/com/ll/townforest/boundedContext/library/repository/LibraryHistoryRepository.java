package com.ll.townforest.boundedContext.library.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.library.entity.LibraryHistory;

public interface LibraryHistoryRepository extends JpaRepository<LibraryHistory, Long> {
	Optional<LibraryHistory> findTopByUserIdAndDateBetweenOrderByDateDesc(
		Long UserId,
		LocalDateTime startOfDay,
		LocalDateTime endOfDay
	);

	Optional<LibraryHistory> findTopByUserIdOrderByIdDesc(Long aptAccountId);

	Slice<LibraryHistory> findByUserIdOrderByIdDesc(Long aptAccountId, Pageable pageable);

	Page<LibraryHistory> findAllByOrderByIdDesc(Pageable pageable);

	Optional<LibraryHistory> findTopBySeatIdAndDateBetween(Long id, LocalDateTime startOfDay, LocalDateTime endOfDay);
}