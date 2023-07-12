package com.ll.townforest.boundedContext.apt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.apt.entity.Apt;
import com.ll.townforest.boundedContext.apt.entity.House;

public interface HouseRepository extends JpaRepository<House, Long> {
	Optional<House> findByAptAndDongAndHo(Apt apt, Integer Dong, Integer Ho);
}
