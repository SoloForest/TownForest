package com.ll.townforest.boundedContext.apt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.apt.entity.House;

public interface HouseRepository extends JpaRepository<House, Long> {
	public House findByDongAndHo(Integer Dong, Integer Ho);
}
