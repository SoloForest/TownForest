package com.ll.townforest.boundedContext.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.gym.entity.Gym;

public interface GymRepository extends JpaRepository<Gym, Long> {
}
