package com.ll.townforest.boundedContext.apt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;

public interface AptAccountHouseRepository extends JpaRepository<AptAccountHouse, Long> {
}
