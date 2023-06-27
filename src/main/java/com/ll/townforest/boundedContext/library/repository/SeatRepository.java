package com.ll.townforest.boundedContext.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.library.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
