package com.ll.townforest.boundedContext.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.library.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
	List<Seat> findByLibraryIdAndStatus(Long libraryId, int status);

	Optional<Seat> findBySeatNumber(int seatNumber);

	List<Seat> findByStatus(int status);
}