package com.ll.townforest.boundedContext.apt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.entity.House;

public interface AptAccountHouseRepository extends JpaRepository<AptAccountHouse, Long> {
	Optional<AptAccountHouse> findByUserId(Long id);

	List<AptAccountHouse> findAllByHouseId(Long houseId);

	List<AptAccountHouse> findByUser_StatusTrueOrderByUserIdDesc();

	List<AptAccountHouse> findByUser_StatusFalseOrderByUserIdDesc();

	List<AptAccountHouse> findAllByOrderByIdDesc();

	Optional<AptAccountHouse> findByUser(AptAccount aptAccount);

	List<AptAccountHouse> findAllByHouse(House house);
}