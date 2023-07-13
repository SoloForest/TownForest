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

	List<AptAccountHouse> findByUser_StatusTrueAndStatusFalseOrderByUserIdDesc();

	List<AptAccountHouse> findByUser_StatusFalseAndStatusFalseOrderByUserIdDesc();

	List<AptAccountHouse> findAllByStatusFalseOrderByIdDesc();

	Optional<AptAccountHouse> findByUser(AptAccount aptAccount);

	List<AptAccountHouse> findAllByHouseAndStatusFalse(House house);

	Optional<AptAccountHouse> findByHouseAndRelationshipAndStatusFalse(House house, String relationship);

	List<AptAccountHouse> findAllByHouseIdAndStatusFalse(Long houseId);
}