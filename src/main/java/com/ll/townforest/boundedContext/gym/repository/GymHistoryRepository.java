package com.ll.townforest.boundedContext.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.gym.entity.GymHistory;

public interface GymHistoryRepository extends JpaRepository<GymHistory, Long> {
}
