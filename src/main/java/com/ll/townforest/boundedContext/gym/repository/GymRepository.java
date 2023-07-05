package com.ll.townforest.boundedContext.gym.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.gym.entity.Gym;

public interface GymRepository extends JpaRepository<Gym, Long> {
	Optional<Gym> findByAptId(Long aptId);
}
