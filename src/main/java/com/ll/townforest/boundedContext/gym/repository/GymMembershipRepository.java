package com.ll.townforest.boundedContext.gym.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.gym.entity.GymMembership;

public interface GymMembershipRepository extends JpaRepository<GymMembership, Long> {
	Optional<GymMembership> findByUserId(Long userId);

	List<GymMembership> findAllByEndDateAndStatusNot(LocalDate endDate, int status);

	List<GymMembership> findAllByStartDateAndStatus(LocalDate today, int status);
}
