package com.ll.townforest.boundedContext.apt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.entity.House;

public interface AptAccountHouseRepository extends JpaRepository<AptAccountHouse, Long> {
	Optional<AptAccountHouse> findByUserId(Long id);

	List<AptAccountHouse> findAllByHouseId(Long houseId);

	Page<AptAccountHouse> findByUser_StatusTrueOrderByUserIdDesc(Pageable pageable);

	Page<AptAccountHouse> findByUser_StatusFalseOrderByUserIdDesc(Pageable pageable);

	Page<AptAccountHouse> findAllByOrderByIdDesc(Pageable pageable);

	Optional<AptAccountHouse> findByUser(AptAccount aptAccount);

	List<AptAccountHouse> findAllByHouse(House house);

	Optional<AptAccountHouse> findByHouseAndRelationship(House house, String relationship);
}