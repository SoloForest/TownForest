package com.ll.townforest.boundedContext.maintenance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.maintenance.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	List<Vehicle> findByAptHouse_House_Id(Long houseId);
}
