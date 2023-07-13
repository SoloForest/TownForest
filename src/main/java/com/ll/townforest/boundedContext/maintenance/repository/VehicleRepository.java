package com.ll.townforest.boundedContext.maintenance.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.maintenance.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	List<Vehicle> findByAptHouse_House_Id(Long houseId);

	Page<Vehicle> findByTypeAndDateBefore(int type, LocalDateTime endDate, Pageable pageable);

	Page<Vehicle> findByTypeAndDateBetween(int type, LocalDateTime startOfDay, LocalDateTime endOfDay,
		Pageable pageable);

	Page<Vehicle> findByTypeAndDateGreaterThanEqual(int type, LocalDateTime startDate, Pageable pageable);

	Slice<Vehicle> findByUserIdAndTypeOrderByIdDesc(Long aptAccountId, int type, Pageable pageable);

	long deleteAllByName(String userName);
}
