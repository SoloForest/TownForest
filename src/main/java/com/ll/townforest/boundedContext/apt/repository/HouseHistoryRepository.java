package com.ll.townforest.boundedContext.apt.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.townforest.boundedContext.apt.entity.House;
import com.ll.townforest.boundedContext.apt.entity.HouseHistory;

public interface HouseHistoryRepository extends JpaRepository<HouseHistory, Long> {
	@Query("SELECT h FROM House h WHERE h.type = 1 AND NOT EXISTS (SELECT hh FROM HouseHistory hh WHERE hh.house.id = h.id AND hh.selectedDate = :selectedDate AND hh.status = 1)")
	List<House> findCanBookingHousesWithDate(@Param("selectedDate") LocalDateTime selectedDate);

	Slice<HouseHistory> findByUserIdOrderByIdDesc(Long userId, PageRequest of);

	List<HouseHistory> findByUserIdAndSelectedDateBetween(
		Long aptAccountId,
		LocalDateTime startOfDay,
		LocalDateTime endOfDay
	);

	List<HouseHistory> findByHouseIdAndStatusAndSelectedDateBetween(
		Long houseId,
		int status,
		LocalDateTime startOfDay,
		LocalDateTime endOfDay
	);
}
