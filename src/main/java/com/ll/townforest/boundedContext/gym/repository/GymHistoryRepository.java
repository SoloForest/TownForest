package com.ll.townforest.boundedContext.gym.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ll.townforest.boundedContext.gym.entity.GymHistory;

public interface GymHistoryRepository extends JpaRepository<GymHistory, Long> {
	Page<GymHistory> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);
}
