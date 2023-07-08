package com.ll.townforest.boundedContext.apt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;

public interface AptAccountHouseRepository extends JpaRepository<AptAccountHouse, Long> {
	Optional<AptAccountHouse> findByUserId(Long id);

	@Query("SELECT a FROM AptAccountHouse a WHERE a.user.status = true ORDER BY a.id DESC")
	List<AptAccountHouse> findByUser_StatusTrue();

	@Query("SELECT a FROM AptAccountHouse a WHERE a.user.status = false ORDER BY a.id DESC")
	List<AptAccountHouse> findByUser_StatusFalse();

	@Query("SELECT a FROM AptAccountHouse a ORDER BY a.id DESC")
	List<AptAccountHouse> findAllDesc();
}